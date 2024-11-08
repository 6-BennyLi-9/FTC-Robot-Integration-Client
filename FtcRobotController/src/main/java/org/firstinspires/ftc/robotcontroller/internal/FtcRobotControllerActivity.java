/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.robotcontroller.internal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.blocks.ftcrobotcontroller.ProgrammingWebHandlers;
import com.google.blocks.ftcrobotcontroller.runtime.BlocksOpMode;
import com.qualcomm.ftccommon.ClassManagerFactory;
import com.qualcomm.ftccommon.FtcAboutActivity;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.ftccommon.FtcEventLoopIdle;
import com.qualcomm.ftccommon.FtcRobotControllerService;
import com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity;
import com.qualcomm.ftccommon.LaunchActivityConstantsList;
import com.qualcomm.ftccommon.Restarter;
import com.qualcomm.ftccommon.UpdateUI;
import com.qualcomm.ftccommon.configuration.EditParameters;
import com.qualcomm.ftccommon.configuration.FtcLoadFileActivity;
import com.qualcomm.ftccommon.configuration.RobotConfigFile;
import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
import com.qualcomm.ftcrobotcontroller.BuildConfig;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.FtcRobotControllerServiceState;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.ClockWarningSource;
import com.qualcomm.robotcore.util.Device;
import com.qualcomm.robotcore.util.Dimmer;
import com.qualcomm.robotcore.util.ImmersiveMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.WebServer;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
import com.qualcomm.robotcore.wifi.NetworkType;

import org.firstinspires.ftc.ftccommon.external.SoundPlayingRobotMonitor;
import org.firstinspires.ftc.ftccommon.internal.AnnotatedHooksClassFilter;
import org.firstinspires.ftc.ftccommon.internal.FtcRobotControllerWatchdogService;
import org.firstinspires.ftc.ftccommon.internal.ProgramAndManageActivity;
import org.firstinspires.ftc.onbotjava.ExternalLibraries;
import org.firstinspires.ftc.onbotjava.OnBotJavaHelperImpl;
import org.firstinspires.ftc.onbotjava.OnBotJavaProgrammingMode;
import org.firstinspires.ftc.robotcore.external.navigation.MotionDetection;
import org.firstinspires.ftc.robotcore.internal.hardware.android.AndroidBoard;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.PreferenceRemoterRC;
import org.firstinspires.ftc.robotcore.internal.network.StartResult;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectChannelChanger;
import org.firstinspires.ftc.robotcore.internal.network.WifiMuteEvent;
import org.firstinspires.ftc.robotcore.internal.network.WifiMuteStateMachine;
import org.firstinspires.ftc.robotcore.internal.opmode.ClassManager;
import org.firstinspires.ftc.robotcore.internal.opmode.OnBotJavaHelper;
import org.firstinspires.ftc.robotcore.internal.system.AppAliveNotifier;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;
import org.firstinspires.ftc.robotcore.internal.system.ServiceController;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.firstinspires.ftc.robotcore.internal.webserver.RobotControllerWebInfo;
import org.firstinspires.ftc.robotserver.internal.programmingmode.ProgrammingModeManager;
import org.firstinspires.inspection.RcInspectionActivity;
import org.threeten.bp.YearMonth;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("WeakerAccess")
public class FtcRobotControllerActivity extends Activity
  {
  public static final String TAG = "RCActivity";
  public String getTag() { return TAG; }

  private static final int REQUEST_CONFIG_WIFI_CHANNEL = 1;
  private static final int NUM_GAMEPADS = 2;

  protected WifiManager.WifiLock wifiLock;
  protected RobotConfigFileManager cfgFileMgr;

  private OnBotJavaHelper onBotJavaHelper;

  protected ProgrammingModeManager programmingModeManager;

  protected UpdateUI.Callback callback;
  protected Context context;
  protected Utility utility;
  protected StartResult prefRemoterStartResult = new StartResult();
  protected StartResult deviceNameStartResult = new StartResult();
  protected PreferencesHelper preferencesHelper;
  protected final SharedPreferencesListener sharedPreferencesListener = new SharedPreferencesListener();

  protected ImageButton buttonMenu;
  protected TextView textDeviceName;
  protected TextView textNetworkConnectionStatus;
  protected TextView textRobotStatus;
  protected TextView[] textGamepad = new TextView[NUM_GAMEPADS];
  protected TextView textOpMode;
  protected TextView textErrorMessage;
  protected ImmersiveMode immersion;

  protected UpdateUI updateUI;
  protected Dimmer dimmer;
  protected LinearLayout entireScreenLayout;

  protected FtcRobotControllerService controllerService;
  protected NetworkType networkType;

  protected FtcEventLoop eventLoop;
  protected Queue<UsbDevice> receivedUsbAttachmentNotifications;

  protected WifiMuteStateMachine wifiMuteStateMachine;
  protected MotionDetection motionDetection;

  private static boolean permissionsValidated;

  private WifiDirectChannelChanger wifiDirectChannelChanger;

  protected class RobotRestarter implements Restarter {

    public void requestRestart() {
	    FtcRobotControllerActivity.this.requestRobotRestart();
    }

  }

  protected boolean serviceShouldUnbind;
  protected ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
      final FtcRobotControllerService.FtcRobotControllerBinder binder = (FtcRobotControllerService.FtcRobotControllerBinder) service;
	    FtcRobotControllerActivity.this.onServiceBind(binder.getService());
    }

    @Override
    public void onServiceDisconnected(final ComponentName name) {
      RobotLog.vv(FtcRobotControllerService.TAG, "%s.controllerService=null", TAG);
	    FtcRobotControllerActivity.this.controllerService = null;
    }
  };

  @Override
  protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);

    if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) {
      final UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
      RobotLog.vv(TAG, "ACTION_USB_DEVICE_ATTACHED: %s", usbDevice.getDeviceName());

      if (null != usbDevice) {  // paranoia
        // We might get attachment notifications before the event loop is set up, so
        // we hold on to them and pass them along only when we're good and ready.
        if (null != receivedUsbAttachmentNotifications) { // *total* paranoia
	        this.receivedUsbAttachmentNotifications.add(usbDevice);
	        this.passReceivedUsbAttachmentsToEventLoop();
        }
      }
    }
  }

  protected void passReceivedUsbAttachmentsToEventLoop() {
    if (null != this.eventLoop) {
      for (;;) {
        final UsbDevice usbDevice = this.receivedUsbAttachmentNotifications.poll();
        if (null == usbDevice)
          break;
        eventLoop.onUsbDeviceAttached(usbDevice);
      }
    }
    else {
      // Paranoia: we don't want the pending list to grow without bound when we don't
      // (yet) have an event loop
      while (100 < receivedUsbAttachmentNotifications.size()) {
	      this.receivedUsbAttachmentNotifications.poll();
      }
    }
  }

  /**
   * There are cases where a permission may be revoked and the system restart will restart the
   * FtcRobotControllerActivity, instead of the launch activity.  Detect when that happens, and throw
   * the device back to the permission validator activity.
   */
  protected boolean enforcePermissionValidator() {
    if (! permissionsValidated) {
      RobotLog.vv(TAG, "Redirecting to permission validator");
      final Intent permissionValidatorIntent = new Intent(AppUtil.getDefContext(), PermissionValidatorWrapper.class);
	    this.startActivity(permissionValidatorIntent);
	    this.finish();
      return true;
    } else {
      RobotLog.vv(TAG, "Permissions validated already");
      return false;
    }
  }

  public static void setPermissionsValidated() {
	  permissionsValidated = true;
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (this.enforcePermissionValidator()) {
      return;
    }

    RobotLog.onApplicationStart();  // robustify against onCreate() following onDestroy() but using the same app instance, which apparently does happen
    RobotLog.vv(TAG, "onCreate()");
    ThemedActivity.appAppThemeToActivity(TAG, this); // do this way instead of inherit to help AppInventor

    // Oddly, sometimes after a crash & restart the root activity will be something unexpected, like from the before crash? We don't yet understand
    RobotLog.vv(TAG, "rootActivity is of class %s", AppUtil.getInstance().getRootActivity().getClass().getSimpleName());
    RobotLog.vv(TAG, "launchActivity is of class %s", FtcRobotControllerWatchdogService.launchActivity());
    Assert.assertTrue(FtcRobotControllerWatchdogService.isLaunchActivity(AppUtil.getInstance().getRootActivity()));
    Assert.assertTrue(AppUtil.getInstance().isRobotController());

    // Quick check: should we pretend we're not here, and so allow the Lynx to operate as
    // a stand-alone USB-connected module?
    if (LynxConstants.isRevControlHub()) {
      // Double-sure check that we can talk to the DB over the serial TTY
      AndroidBoard.getInstance().getAndroidBoardIsPresentPin().setState(true);
    }

	  this.context = this;
	  this.utility = new Utility(this);

    DeviceNameManagerFactory.getInstance().start(this.deviceNameStartResult);

    PreferenceRemoterRC.getInstance().start(this.prefRemoterStartResult);

	  this.receivedUsbAttachmentNotifications = new ConcurrentLinkedQueue<UsbDevice>();
	  this.eventLoop = null;

	  this.setContentView(R.layout.activity_ftc_controller);

	  this.preferencesHelper = new PreferencesHelper(TAG, this.context);
	  this.preferencesHelper.writeBooleanPrefIfDifferent(this.context.getString(R.string.pref_rc_connected), true);
	  this.preferencesHelper.getSharedPreferences().registerOnSharedPreferenceChangeListener(this.sharedPreferencesListener);

    // Check if this RC app is from a later FTC season than what was installed previously
    final int ftcSeasonYearOfPreviouslyInstalledRc = this.preferencesHelper.readInt(this.getString(R.string.pref_ftc_season_year_of_current_rc), 0);
    final int ftcSeasonYearOfCurrentlyInstalledRc  = AppUtil.getInstance().getFtcSeasonYear(AppUtil.getInstance().getLocalSdkBuildMonth()).getValue();
    if (ftcSeasonYearOfCurrentlyInstalledRc > ftcSeasonYearOfPreviouslyInstalledRc) {
	    this.preferencesHelper.writeIntPrefIfDifferent(this.getString(R.string.pref_ftc_season_year_of_current_rc), ftcSeasonYearOfCurrentlyInstalledRc);
      // Since it's a new FTC season, we should reset certain settings back to their default values.
	    this.preferencesHelper.writeBooleanPrefIfDifferent(this.getString(R.string.pref_warn_about_2_4_ghz_band), true);
	    this.preferencesHelper.writeBooleanPrefIfDifferent(this.getString(R.string.pref_warn_about_obsolete_software), true);
	    this.preferencesHelper.writeBooleanPrefIfDifferent(this.getString(R.string.pref_warn_about_mismatched_app_versions), true);
	    this.preferencesHelper.writeBooleanPrefIfDifferent(this.getString(R.string.pref_warn_about_incorrect_clocks), true);
    }

	  this.entireScreenLayout = this.findViewById(R.id.entire_screen);
	  this.buttonMenu = this.findViewById(R.id.menu_buttons);
	  this.buttonMenu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        final PopupMenu popupMenu = new PopupMenu(FtcRobotControllerActivity.this, v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(final MenuItem item) {
            return FtcRobotControllerActivity.this.onOptionsItemSelected(item); // Delegate to the handler for the hardware menu button
          }
        });
        popupMenu.inflate(R.menu.ftc_robot_controller);
        AnnotatedHooksClassFilter.getInstance().callOnCreateMenuMethods(
            FtcRobotControllerActivity.this, popupMenu.getMenu());
        popupMenu.show();
      }
    });

	  this.updateMonitorLayout(this.getResources().getConfiguration());

    BlocksOpMode.setActivityAndWebView(this, this.findViewById(R.id.webViewBlocksRuntime));

    ExternalLibraries.getInstance().onCreate();
	  this.onBotJavaHelper = new OnBotJavaHelperImpl();

    /*
     * Paranoia as the ClassManagerFactory requires EXTERNAL_STORAGE permissions
     * and we've seen on the DS where the finish() call above does not short-circuit
     * the onCreate() call for the activity and then we crash here because we don't
     * have permissions. So...
     */
    if (permissionsValidated) {
      ClassManager.getInstance().setOnBotJavaClassHelper(this.onBotJavaHelper);
      ClassManagerFactory.registerFilters();
      ClassManagerFactory.processAllClasses();
    }

	  this.cfgFileMgr = new RobotConfigFileManager(this);

    // Clean up 'dirty' status after a possible crash
    final RobotConfigFile configFile = this.cfgFileMgr.getActiveConfig();
    if (configFile.isDirty()) {
      configFile.markClean();
	    this.cfgFileMgr.setActiveConfig(false, configFile);
    }

	  this.textDeviceName = this.findViewById(R.id.textDeviceName);
	  this.textNetworkConnectionStatus = this.findViewById(R.id.textNetworkConnectionStatus);
	  this.textRobotStatus = this.findViewById(R.id.textRobotStatus);
	  this.textOpMode = this.findViewById(R.id.textOpMode);
	  this.textErrorMessage = this.findViewById(R.id.textErrorMessage);
	  this.textGamepad[0] = this.findViewById(R.id.textGamepad1);
	  this.textGamepad[1] = this.findViewById(R.id.textGamepad2);
	  this.immersion = new ImmersiveMode(this.getWindow().getDecorView());
	  this.dimmer = new Dimmer(this);
	  this.dimmer.longBright();

	  this.programmingModeManager = new ProgrammingModeManager();
	  this.programmingModeManager.register(new ProgrammingWebHandlers());
	  this.programmingModeManager.register(new OnBotJavaProgrammingMode());

	  this.updateUI = this.createUpdateUI();
	  this.callback = this.createUICallback(this.updateUI);

    PreferenceManager.setDefaultValues(this, R.xml.app_settings, false);

    final WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
	  this.wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "");

	  this.hittingMenuButtonBrightensScreen();

	  this.wifiLock.acquire();
	  this.callback.networkConnectionUpdate(NetworkConnection.NetworkEvent.DISCONNECTED);
	  this.readNetworkType();
    ServiceController.startService(FtcRobotControllerWatchdogService.class);
	  this.bindToService();
    RobotLog.logAppInfo();
    RobotLog.logDeviceInfo();
    AndroidBoard.getInstance().logAndroidBoardInfo();

    if (this.preferencesHelper.readBoolean(this.getString(R.string.pref_wifi_automute), false)) {
	    this.initWifiMute(true);
    }

    FtcAboutActivity.setBuildTimeFromBuildConfig(BuildConfig.APP_BUILD_TIME);

    // check to see if there is a preferred Wi-Fi to use.
	  this.checkPreferredChannel();

    AnnotatedHooksClassFilter.getInstance().callOnCreateMethods(this);
  }

  protected UpdateUI createUpdateUI() {
    final Restarter restarter = new RobotRestarter();
    final UpdateUI  result    = new UpdateUI(this, this.dimmer);
    result.setRestarter(restarter);
    result.setTextViews(this.textNetworkConnectionStatus, this.textRobotStatus, this.textGamepad, this.textOpMode, this.textErrorMessage, this.textDeviceName);
    return result;
  }

  protected UpdateUI.Callback createUICallback(final UpdateUI updateUI) {
    final UpdateUI.Callback result = updateUI.new Callback();
    result.setStateMonitor(new SoundPlayingRobotMonitor());
    return result;
  }

  @Override
  protected void onStart() {
    super.onStart();
    RobotLog.vv(TAG, "onStart()");

	  this.entireScreenLayout.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(final View v, final MotionEvent event) {
	      FtcRobotControllerActivity.this.dimmer.handleDimTimer();
        return false;
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    RobotLog.vv(TAG, "onResume()");

    // In case the user just got back from fixing their clock, refresh ClockWarningSource
    ClockWarningSource.getInstance().onPossibleRcClockUpdate();
  }

  @Override
  protected void onPause() {
    super.onPause();
    RobotLog.vv(TAG, "onPause()");
  }

  @Override
  protected void onStop() {
    // Note: this gets called even when the configuration editor is launched. That is, it gets
    // called surprisingly often. So, we don't actually do much here.
    super.onStop();
    RobotLog.vv(TAG, "onStop()");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    RobotLog.vv(TAG, "onDestroy()");

	  this.shutdownRobot();  // Ensure the robot is put away to bed
    if (null != callback) this.callback.close();

    PreferenceRemoterRC.getInstance().stop(this.prefRemoterStartResult);
    DeviceNameManagerFactory.getInstance().stop(this.deviceNameStartResult);

	  this.unbindFromService();
    // If the app manually (?) is stopped, then we don't need the auto-starting function (?)
    ServiceController.stopService(FtcRobotControllerWatchdogService.class);
    if (null != wifiLock) this.wifiLock.release();
    if (null != preferencesHelper) this.preferencesHelper.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this.sharedPreferencesListener);

    RobotLog.cancelWriteLogcatToDisk();

    AnnotatedHooksClassFilter.getInstance().callOnDestroyMethods(this);
  }

  protected void bindToService() {
	  this.readNetworkType();
    final Intent intent = new Intent(this, FtcRobotControllerService.class);
    intent.putExtra(NetworkConnectionFactory.NETWORK_CONNECTION_TYPE, this.networkType);
	  this.serviceShouldUnbind = this.bindService(intent, this.connection, BIND_AUTO_CREATE);
  }

  protected void unbindFromService() {
    if (this.serviceShouldUnbind) {
	    this.unbindService(this.connection);
	    this.serviceShouldUnbind = false;
    }
  }

  protected void readNetworkType() {
    // Control hubs are always running the access point model.  Everything else, for the time
    // being always runs the Wi-Fi Direct model.
    if (Device.isRevControlHub()) {
	    this.networkType = NetworkType.RCWIRELESSAP;
    } else {
	    this.networkType = NetworkType.fromString(this.preferencesHelper.readString(this.context.getString(R.string.pref_pairing_kind), NetworkType.globalDefaultAsString()));
    }

    // update the app_settings
	  this.preferencesHelper.writeStringPrefIfDifferent(this.context.getString(R.string.pref_pairing_kind), this.networkType.toString());
  }

  @Override
  public void onWindowFocusChanged(final boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    if (hasFocus) {
	    this.immersion.hideSystemUI();
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
	  this.getMenuInflater().inflate(R.menu.ftc_robot_controller, menu);
    AnnotatedHooksClassFilter.getInstance().callOnCreateMenuMethods(this, menu);
    return true;
  }

  private boolean isRobotRunning() {
    if (null == controllerService) {
      return false;
    }

    final Robot robot = this.controllerService.getRobot();

    if ((null == robot) || (null == robot.eventLoopManager)) {
      return false;
    }

    final RobotState robotState = robot.eventLoopManager.state;

	  return RobotState.RUNNING == robotState;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    final int id = item.getItemId();

    if (id == R.id.action_program_and_manage) {
      if (this.isRobotRunning()) {
        final Intent                 programmingModeIntent = new Intent(AppUtil.getDefContext(), ProgramAndManageActivity.class);
        final RobotControllerWebInfo webInfo               = this.programmingModeManager.getWebServer().getConnectionInformation();
        programmingModeIntent.putExtra(LaunchActivityConstantsList.RC_WEB_INFO, webInfo.toJson());
	      this.startActivity(programmingModeIntent);
      } else {
        AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, this.context.getString(R.string.toastWifiUpBeforeProgrammingMode));
      }
    } else if (id == R.id.action_inspection_mode) {
      final Intent inspectionModeIntent = new Intent(AppUtil.getDefContext(), RcInspectionActivity.class);
	    this.startActivity(inspectionModeIntent);
      return true;
    } else if (id == R.id.action_restart_robot) {
	    this.dimmer.handleDimTimer();
      AppUtil.getInstance().showToast(UILocation.BOTH, this.context.getString(R.string.toastRestartingRobot));
	    this.requestRobotRestart();
      return true;
    }
    else if (id == R.id.action_configure_robot) {
      final EditParameters parameters      = new EditParameters();
      final Intent         intentConfigure = new Intent(AppUtil.getDefContext(), FtcLoadFileActivity.class);
      parameters.putIntent(intentConfigure);
	    this.startActivityForResult(intentConfigure, LaunchActivityConstantsList.RequestCode.CONFIGURE_ROBOT_CONTROLLER.ordinal());
    }
    else if (id == R.id.action_settings) {
	  // historical: this once erroneously used FTC_CONFIGURE_REQUEST_CODE_ROBOT_CONTROLLER
      final Intent settingsIntent = new Intent(AppUtil.getDefContext(), FtcRobotControllerSettingsActivity.class);
      startActivityForResult(settingsIntent, LaunchActivityConstantsList.RequestCode.SETTINGS_ROBOT_CONTROLLER.ordinal());
      return true;
    }
    else if (id == R.id.action_about) {
      final Intent intent = new Intent(AppUtil.getDefContext(), FtcAboutActivity.class);
      startActivity(intent);
      return true;
    }
    else if (id == R.id.action_exit_app) {

      //Clear backstack and everything to prevent edge case where VM might be
      //restarted (after it was exited) if more than one activity was on the
      //backstack for some reason.
      finishAffinity();

      //For lollipop and up, we can clear ourselves from the recents list too
      if (android.os.Build.VERSION_CODES.LOLLIPOP <= android.os.Build.VERSION.SDK_INT) {
        final ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.AppTask> tasks = manager.getAppTasks();

        for (final ActivityManager.AppTask task : tasks) {
          task.finishAndRemoveTask();
        }
      }

      // Allow the user to use the Control Hub operating system's UI, instead of relaunching the app
      AppAliveNotifier.getInstance().disableAppWatchdogUntilNextAppStart();

      //Finally, nuke the VM from orbit
      AppUtil.getInstance().exitApplication();

      return true;
    }

   return super.onOptionsItemSelected(item);
  }

  @Override
  public void onConfigurationChanged(final Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // don't destroy assets on screen rotation
    updateMonitorLayout(newConfig);
  }

  /**
   * Updates the orientation of monitorContainer (which contains cameraMonitorView)
   * based on the given configuration. Makes the children split the space.
   */
  private void updateMonitorLayout(final Configuration configuration) {
    final LinearLayout monitorContainer = findViewById(R.id.monitorContainer);
    if (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation) {
      // When the phone is landscape, lay out the monitor views horizontally.
      monitorContainer.setOrientation(LinearLayout.HORIZONTAL);
      for (int i = 0; i < monitorContainer.getChildCount(); i++) {
        final View view = monitorContainer.getChildAt(i);
        view.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1 /* weight */));
      }
    } else {
      // When the phone is portrait, lay out the monitor views vertically.
      monitorContainer.setOrientation(LinearLayout.VERTICAL);
      for (int i = 0; i < monitorContainer.getChildCount(); i++) {
        final View view = monitorContainer.getChildAt(i);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1 /* weight */));
      }
    }
    monitorContainer.requestLayout();
  }

  @Override
  protected void onActivityResult(final int request, final int result, final Intent intent) {
    if (REQUEST_CONFIG_WIFI_CHANNEL == request) {
      if (RESULT_OK == result) {
        AppUtil.getInstance().showToast(UILocation.BOTH, context.getString(R.string.toastWifiConfigurationComplete));
      }
    }
    // was some historical confusion about launch codes here, so we err safely
    if (request == LaunchActivityConstantsList.RequestCode.CONFIGURE_ROBOT_CONTROLLER.ordinal() || request == LaunchActivityConstantsList.RequestCode.SETTINGS_ROBOT_CONTROLLER.ordinal()) {
      // We always do a refresh, whether it was a cancel or an OK, for robustness
	    this.shutdownRobot();
	    this.cfgFileMgr.getActiveConfigAndUpdateUI();
	    this.updateUIAndRequestRobotSetup();
    }
  }

  public void onServiceBind(final FtcRobotControllerService service) {
    RobotLog.vv(FtcRobotControllerService.TAG, "%s.controllerService=bound", TAG);
	  this.controllerService = service;
	  this.updateUI.setControllerService(this.controllerService);

	  this.controllerService.setOnBotJavaHelper(this.onBotJavaHelper);

	  this.updateUIAndRequestRobotSetup();
	  this.programmingModeManager.setState(new FtcRobotControllerServiceState() {
      @NonNull
      @Override
      public WebServer getWebServer() {
        return service.getWebServer();
      }

      @Nullable
      @Override
      public OnBotJavaHelper getOnBotJavaHelper() {
        return service.getOnBotJavaHelper();
      }

      @Override
      public EventLoopManager getEventLoopManager() {
        return service.getRobot().eventLoopManager;
      }
    });

    AnnotatedHooksClassFilter.getInstance().callWebHandlerRegistrarMethods(this,
        service.getWebServer().getWebHandlerManager());
  }

  private void updateUIAndRequestRobotSetup() {
    if (null != controllerService) {
	    this.callback.networkConnectionUpdate(this.controllerService.getNetworkConnectionStatus());
	    this.callback.updateRobotStatus(this.controllerService.getRobotStatus());
      // Only show this first-time toast on headless systems: what we have now on non-headless suffices
	    this.requestRobotSetup(LynxConstants.isRevControlHub()
        ? new Runnable() {
            @Override public void run() {
	            FtcRobotControllerActivity.this.showRestartRobotCompleteToast(R.string.toastRobotSetupComplete);
            }
          }
        : null);
    }
  }

  private void requestRobotSetup(@Nullable final Runnable runOnComplete) {
    if (null == controllerService) return;

    RobotConfigFile       file            = this.cfgFileMgr.getActiveConfigAndUpdateUI();
    final HardwareFactory hardwareFactory = new HardwareFactory(this.context);
    try {
      hardwareFactory.setXmlPullParser(file.getXml());
    } catch (final FileNotFoundException | XmlPullParserException e) {
      RobotLog.ww(TAG, e, "Unable to set configuration file %s. Falling back on noConfig.", file.getName());
      file = RobotConfigFile.noConfig(this.cfgFileMgr);
      try {
        hardwareFactory.setXmlPullParser(file.getXml());
	      this.cfgFileMgr.setActiveConfigAndUpdateUI(false, file);
      } catch (final FileNotFoundException | XmlPullParserException e1) {
        RobotLog.ee(TAG, e1, "Failed to fall back on noConfig");
      }
    }

    final OpModeRegister userOpModeRegister = this.createOpModeRegister();
	  this.eventLoop = new FtcEventLoop(hardwareFactory, userOpModeRegister, this.callback, this);
    final FtcEventLoopIdle idleLoop = new FtcEventLoopIdle(hardwareFactory, userOpModeRegister, this.callback, this);

	  this.controllerService.setCallback(this.callback);
	  this.controllerService.setupRobot(this.eventLoop, idleLoop, runOnComplete);

	  this.passReceivedUsbAttachmentsToEventLoop();
    AndroidBoard.showErrorIfUnknownControlHub();

    AnnotatedHooksClassFilter.getInstance().callOnCreateEventLoopMethods(this, this.eventLoop);
  }

  protected OpModeRegister createOpModeRegister() {
    return new FtcOpModeRegister();
  }

  private void shutdownRobot() {
    if (null != controllerService) this.controllerService.shutdownRobot();
  }

  private void requestRobotRestart() {
    AppUtil.getInstance().showToast(UILocation.BOTH, AppUtil.getDefContext().getString(R.string.toastRestartingRobot));
    //
    RobotLog.clearGlobalErrorMsg();
    RobotLog.clearGlobalWarningMsg();
	  this.shutdownRobot();
	  this.requestRobotSetup(new Runnable() {
      @Override public void run() {
	      FtcRobotControllerActivity.this.showRestartRobotCompleteToast(R.string.toastRestartRobotComplete);
        }
      });
  }

  private void showRestartRobotCompleteToast(@StringRes final int resid) {
    AppUtil.getInstance().showToast(UILocation.BOTH, AppUtil.getDefContext().getString(resid));
  }

  private void checkPreferredChannel() {
    // For P2P network, check to see what preferred channel is.
    if (NetworkType.WIFIDIRECT == networkType) {
      int prefChannel = this.preferencesHelper.readInt(this.getString(com.qualcomm.ftccommon.R.string.pref_wifip2p_channel), -1);
      if (- 1 == prefChannel) {
        prefChannel = 0;
        RobotLog.vv(TAG, "pref_wifip2p_channel: No preferred channel defined. Will use a default value of %d", prefChannel);
      } else {
        RobotLog.vv(TAG, "pref_wifip2p_channel: Found existing preferred channel (%d).", prefChannel);
      }

      // attempt to set the preferred channel.
      RobotLog.vv(TAG, "pref_wifip2p_channel: attempting to set preferred channel...");
	    this.wifiDirectChannelChanger = new WifiDirectChannelChanger();
	    this.wifiDirectChannelChanger.changeToChannel(prefChannel);
    }
  }

  protected void hittingMenuButtonBrightensScreen() {
    final ActionBar actionBar = this.getActionBar();
    if (null != actionBar) {
      actionBar.addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {
        @Override
        public void onMenuVisibilityChanged(final boolean isVisible) {
          if (isVisible) {
	          FtcRobotControllerActivity.this.dimmer.handleDimTimer();
          }
        }
      });
    }
  }

  protected class SharedPreferencesListener implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
      if (key.equals(FtcRobotControllerActivity.this.context.getString(R.string.pref_app_theme))) {
        ThemedActivity.restartForAppThemeChange(FtcRobotControllerActivity.this.getTag(), FtcRobotControllerActivity.this.getString(R.string.appThemeChangeRestartNotifyRC));
      } else if (key.equals(FtcRobotControllerActivity.this.context.getString(R.string.pref_wifi_automute))) {
	      FtcRobotControllerActivity.this.initWifiMute(FtcRobotControllerActivity.this.preferencesHelper.readBoolean(FtcRobotControllerActivity.this.context.getString(R.string.pref_wifi_automute), false));
      }
    }
  }

  protected void initWifiMute(final boolean enable) {
    if (enable) {
	    this.wifiMuteStateMachine = new WifiMuteStateMachine();
	    this.wifiMuteStateMachine.initialize();
	    this.wifiMuteStateMachine.start();

	    this.motionDetection = new MotionDetection(2.0, 10);
	    this.motionDetection.startListening();
	    this.motionDetection.registerListener(new MotionDetection.MotionDetectionListener() {
        @Override
        public void onMotionDetected(final double vector)
        {
	        FtcRobotControllerActivity.this.wifiMuteStateMachine.consumeEvent(WifiMuteEvent.USER_ACTIVITY);
        }
      });
    } else {
	    this.wifiMuteStateMachine.stop();
	    this.wifiMuteStateMachine = null;
	    this.motionDetection.stopListening();
	    this.motionDetection.purgeListeners();
	    this.motionDetection = null;
    }
  }

  @Override
  public void onUserInteraction() {
    if (null != wifiMuteStateMachine) {
	    this.wifiMuteStateMachine.consumeEvent(WifiMuteEvent.USER_ACTIVITY);
    }
  }
}
