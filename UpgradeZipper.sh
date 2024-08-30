#! /bin/bash
zipperParentPathName=$(dirname "$0")
programPath=${zipperParentPathName}/TeamCode/src/main/java/org/firstinspires/ftc/teamcode

echo ParentPath "${zipperParentPathName}"
echo ProgramPath "${programPath}"

cd "${programPath}" || exit
zip -r RIC_Upgrade.zip ./

mv ./RIC_Upgrade.zip ..
cd .. || echo exit
mv ./RIC_Upgrade.zip ..
cd .. || echo exit
mv ./RIC_Upgrade.zip ..
cd .. || echo exit
mv ./RIC_Upgrade.zip ..
cd .. || echo exit
mv ./RIC_Upgrade.zip ..
cd .. || echo exit
mv ./RIC_Upgrade.zip ..
cd .. || echo exit
mv ./RIC_Upgrade.zip ..
cd .. || echo exit
mv ./RIC_Upgrade.zip ..
cd ..

zip -d RIC_Upgrade Params.java
zip -d RIC_Upgrade Utils/Annotations/TuningOpModes.java
