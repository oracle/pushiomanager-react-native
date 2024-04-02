#!/bin/sh

find_ios_root_path=$(find ../../../ -type d -name ios ! -path "*/node_modules/*" ! -path "*/.git/*" | head -n 1)

default_reactnative_root_path="../../../ios"

generate_command_by_path() {
    rm -rf ./framework/PushIOManager.xcframework && cp -Rf $( echo $1 )/framework/PushIOManager.xcframework ./framework/ || { echo "\n\n\n Error ==> PushIOManager.xcframework not found. Please copy the PushIOManager.xcframework to YOUR_APP_DIR/ios/framework/ and install package again. Follow README.md Installation instructions.<===  \n\n\n;"
    exit 1; }
}

# NOTE: to declare variable by bash need to use export CUSTOM_IOS_PATH_PUSHIOMANAGER = path/to/ios/folder
if [[ "$OSTYPE" == "darwin"* ]]; then
    if [[ ! -z "${CUSTOM_IOS_PATH_PUSHIOMANAGER}" ]]; then
        generate_command_by_path $CUSTOM_IOS_PATH_PUSHIOMANAGER
    elif [[ "$find_ios_root_path" ]]; then
        generate_command_by_path $find_ios_root_path
    else
        generate_command_by_path $default_reactnative_root_path
    fi
fi
