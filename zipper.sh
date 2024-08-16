zipperParentPathName=$(dirname "$0")
programPath=TeamCode/src/main/java/org/firstinspires/ftc/teamcode
cd ${programPath} || exit
rm -i RIC.zip
zip -r RIC.zip DriveControlsAddition/ utils/ RIC_samples/ Hardwares/ RIC_tuning/
cd "${zipperParentPathName}" || exit
mv ${programPath}/RIC.zip "${zipperParentPathName}"