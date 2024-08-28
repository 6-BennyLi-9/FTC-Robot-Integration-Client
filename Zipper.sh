#! /bin/bash
zipperParentPathName=$(dirname "$0")
programPath=${zipperParentPathName}/TeamCode/src/main/java/org/firstinspires/ftc/teamcode

echo ParentPath "${zipperParentPathName}"
echo ProgramPath "${programPath}"

cd "${programPath}" || exit
zip -r RIC.zip ./
mv ./RIC.zip ..
cd .. || echo exit
mv ./RIC.zip ..
cd .. || echo exit
mv ./RIC.zip ..
cd .. || echo exit
mv ./RIC.zip ..
cd .. || echo exit
mv ./RIC.zip ..
cd .. || echo exit
mv ./RIC.zip ..
cd .. || echo exit
mv ./RIC.zip ..
cd .. || echo exit
mv ./RIC.zip ..