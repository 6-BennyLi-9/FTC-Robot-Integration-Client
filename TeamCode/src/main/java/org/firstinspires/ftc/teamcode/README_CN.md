**团队代码模块**

欢迎！

这个模块，团队代码（TeamCode），是你编写/粘贴你的团队机器人控制器应用程序代码的地方。这个模块目前是空的（一张白纸），但添加操作模式（OpModes）的过程是直接的。

**创建你自己的操作模式**

创建你自己的操作模式最简单的方法是复制一个示例操作模式并使其成为你自己的。

示例操作模式存在于FtcRobotController模块中。
要找到这些示例，请在“项目/Android”标签中找到FtcRobotController模块。

展开以下树状元素：
FtcRobotController/java/org.firstinspires.ftc.robotcontroller/external/samples

我们（19419 & 22232）已经在下面为你编写了基础代码。除非你知道你在做什么，否则不要更改它们。

**编码提示**

每个操作模式示例类都以几行代码开始，如下所示：

```java
@TeleOp(name="Template: Linear OpMode", group="Linear Opmode")
@Disabled
class classname{}
```

在驱动站的“操作模式列表”中显示的名称由以下代码定义：
name="Template: Linear OpMode"
你可以更改引号之间的内容，以更好地描述你的操作模式。
代码中的“group=”部分可以用来帮助你组织你的操作模式列表。

如上所示，当前的操作模式不会出现在驱动站的操作模式列表中，因为包含了@Disabled注解。
这行代码可以简单地删除，或注释掉，以使操作模式可见。

**高级多团队应用程序管理：克隆团队代码模块**

在某些情况下，你的俱乐部有多个团队，你希望他们都能共享一个通用的代码组织，每个团队都能看到其他人的代码，但每个团队都有自己的团队模块，他们自己维护自己的代码。

在这种情况下，你可能希望为这些团队中的每一个克隆团队代码模块。
每个克隆模块然后会出现在Android Studio模块列表中，与FtcRobotController模块（和原始的团队代码模块）一起。

选择性的团队手机可以通过在点击绿色运行箭头之前从下拉列表中选择所需的模块来编程。

**警告**：这不是给没有经验的软件开发者使用的。
你需要对文件操作和Android Studio模块管理感到舒适。
这些更改是在Android Studios之外执行的，所以在你开始之前请关闭Android Studios。

另外...开始之前请备份整个项目 :)

**克隆团队代码的方法**：

注意：有些名称以“Team”开头，有些以“team”开头。这是有意为之的。

1) 使用你的操作系统文件管理工具，将整个“TeamCode”文件夹复制到一个具有相应新名称的同级文件夹，例如：“Team0417”。

2) 在新的Team0417文件夹中，删除TeamCode.iml文件。

3) 在新的Team0417文件夹中，将“src/main/java/org/firstinspires/ftc/teamcode”文件夹重命名为一个匹配的名称，使用小写的'team'，例如：“team0417”。

4) 在新的Team0417/src/main文件夹中，编辑“AndroidManifest.xml”文件，将包含以下内容的行
   package="org.firstinspires.ftc.teamcode"
   更改为
   package="org.firstinspires.ftc.team0417"

5) 添加：include ':Team0417' 到“/settings.gradle”文件。

6) 打开Android Studios，通过使用菜单“构建/清理项目”来清除任何旧文件。
