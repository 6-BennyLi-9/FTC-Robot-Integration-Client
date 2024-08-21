zipperParentPathName=$(dirname "$0")
programPath=TeamCode/src/main/java/org/firstinspires/ftc/teamcode

echo ParentPath "${zipperParentPathName}"
echo ProgramPath "${programPath}"

cd ${programPath} || exit
rm -i RIC.zip
zip -r RIC.zip ./
cd "${zipperParentPathName}" || exit
mv ${programPath}/RIC.zip "${zipperParentPathName}"