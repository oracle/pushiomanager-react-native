#!/bin/sh

if [[ "$OSTYPE" == "darwin"* ]]; then
rm -rf ./framework/PushIOManager.framework && cp -Rf { echo $PUSH_IO_MANAGER_PATH_IOS }/framework/PushIOManager.framework ./framework/ || { echo "\n\n\n Error ==> PushIOManager.framework not found. Please copy the PushIOManager.framework to YOUR_APP_DIR/ios/framework/ and install package again. Follow README.md Installation instructions.<===  \n\n\n;"
 exit 1; }
 fi
