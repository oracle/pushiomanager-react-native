# React Native Module for Responsys SDK

This module makes it easy to integrate your React Native based mobile app with the Responsys SDK. 

## Table of Contents
- [Requirements](#requirements)
  * [For Android](#for-android)
  * [For iOS](#for-iOS)
- [Setup](#setup)
  * [For Android](#for-android-1)
  * [For iOS](#for-iOS-1)
- [Installation](#installation)
- [Integration](#integration)
  * [For Android](#for-android-2)
  * [For iOS](#for-iOS-2)
- [Usage](#usage)
  * [Configure And Register](#configure-and-register)
  * [User Identification](#user-identification)
  * [Engagements And Conversion](#engagements-and-conversion)
  * [In-App Messages](#in-app-messages)
  * [Message Center](#message-center)
  * [Geofences And Beacons](#geofences-and-beacons)
  * [Notification Preferences](#notification-preferences)
- [Support](#support)
- [License](#license)


## Requirements

- React Native >= 0.61.5
- React Native CLI >= 2.0.1

### For Android
- Android SDK Tools >= 28.0.3

### For iOS
- iOS 11 or later

## Setup

Before installing the plugin, you must setup your app to receive push notifications.

### For Android
- [Get FCM Credentials](https://docs.oracle.com/en/cloud/saas/marketing/responsys-develop-mobile/android/gcm-credentials) 
- Log in to the [Responsys Mobile App Developer Console](https://docs.oracle.com/en/cloud/saas/marketing/responsys-develop-mobile/dev-console/login/) and enter your FCM credentials (Project ID and Server API Key) for your Android app.
- Download the `pushio_config.json` file generated from your credentials and include it in your project's `android/src/main/assets` folder.
- Copy `PushIOManager-6.45.aar`  and place it in the project's `android/src/main/libs` folder. 


### For iOS
- [Generate Auth Key](https://docs.oracle.com/en/cloud/saas/marketing/responsys-develop-mobile/ios/auth-key/) 
- Log in to the [Responsys Mobile App Developer Console](https://docs.oracle.com/en/cloud/saas/marketing/responsys-develop-mobile/dev-console/login/) and enter your Auth Key and other details for your iOS app.
- Download the `pushio_config.json` file generated from your credentials.
- Copy `PushIOManager.framework`  and place it in the plugin  `PATH_TO_react-native-plugin-pushiomanager_DIRECTORY/PushIOManager/` folder **before** adding plugin to project. 

## Installation

The plugin can be installed with the React Native CLI,

```shell
cd <your_react_native_app>
yarn add <PATH_TO_react-native-pushiomanager_DIRECTORY>
```

### For iOS
- After installing plugin you need to link `PushIOManager` dependency to your project `Podfile`.  Please follow below steps:

    - Open your React Native App Project `Podfile.` Add  the below line
    
     `pod 'PushIOManager', :path => 'PATH_TO_react-native-plugin-pushiomanager_DIRECTORY/PushIOManager/'` after `use_native_modules!`. 
     
     Eg:
    
    ```
    use_native_modules!
    pod 'PushIOManager', :path => '../../PushIOManager/'
    
    ```
    - Run `pod install`


> TODO: Update the steps for NPM

## Integration

### For Android

- Open the `AndroidManifest.xml` file located at `android/src/main` and add the following,
	* Permissions above the `<application>` tag,

		```xml
		<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
		<uses-permission android:name="${applicationId}.permission.PUSHIO_MESSAGE" />
		<uses-permission android:name="${applicationId}.permission.RSYS_SHOW_IAM" />
		<permission android:name=".permission.PUSHIO_MESSAGE" android:protectionLevel="signature" />
		<permission android:name="${applicationId}.permission.RSYS_SHOW_IAM" android:protectionLevel="signature" />
		```
	
	* Intent-filter for launching app when the user taps on a push notification. Add it inside the `<activity>` tag of `MainActivity`,

		```xml
		<intent-filter>
			<action android:name="${applicationId}.NOTIFICATIONPRESSED" />
	   		<category android:name="android.intent.category.DEFAULT" />
		</intent-filter>
		```

	* (Optional) Intent-filter for [Android App Links](https://developer.android.com/training/app-links) setup. Add it inside the `<activity>` tag of `MainActivity`,

		```xml
		<intent-filter android:autoVerify="true">
			<action android:name="android.intent.action.VIEW" />
			<category android:name="android.intent.category.DEFAULT" />
			<category android:name="android.intent.category.BROWSABLE" />
			<data android:host="@string/app_links_url_host" android:pathPrefix="/pub/acc" android:scheme="https" />
       </intent-filter>
		```
		
	* Add the following code inside `<application>` tag,

		```xml
		 <receiver android:enabled="true" android:exported="false" android:name="com.pushio.manager.PushIOUriReceiver" tools:node="replace">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="@string/uri_identifier" />
            </intent-filter>
        </receiver>
        <activity android:name="com.pushio.manager.iam.ui.PushIOMessageViewActivity" android:permission="${applicationId}.permission.SHOW_IAM" android:theme="@android:style/Theme.Translucent.NoTitleBar">
            <intent-filter tools:ignore="AppLinkUrlError">
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.BROWSABLE" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="@string/uri_identifier" />
            </intent-filter>
        </activity>
		```
		

- Open the `strings.xml` file located at `android/src/main/res/values` and add the following properties,

	* Custom URI scheme for displaying In-App Messages and Rich Push content,

		```xml
		<string name="uri_identifier">pio-YOUR_API_KEY</string>
		```
		You can find the API key in the `pushio_config.json` that was placed in `android/app/src/main/assets` earlier during setup.
		
	* (Optional) If you added the `<intent-filter>` for Android App Links in the steps above, then you will need to declare the domain name,
	
		```xml
		<string name="app_links_url_host">YOUR_ANDROID_APP_LINKS_DOMAIN</string>
		```



### For iOS
- Open the Xcode project workspace in your `ios` directory of cordova app. 
- Drag and Drop your `pushio_config.json` in Xcode project.
- Select the root project and Under Capabilites add the "Push Notifications" and "Background Modes". 
![Capability Image](./img/ios_add_capability.png "Capabilty Image")
- For In-App Messages and Rich Push Content follow the below steps :
  * To Enable Custom URI scheme for displaying In-App Messages and Rich Push content follow the [Step 1](https://docs.oracle.com/en/cloud/saas/marketing/responsys-develop-mobile/ios/in-app-msg/). You don't need to add the code.
  You can find the API key in the `pushio_config.json` that was placed in your Xcode project earlier during setup.
  
  * Follow  [Step 2](https://docs.oracle.com/en/cloud/saas/marketing/responsys-develop-mobile/ios/in-app-msg/) to  add the required capabilites in your Xcode project for In-App messages. You don't need to add the code.

- For Media Attachments you can follow the following [guide](https://docs.oracle.com/en/cloud/saas/marketing/responsys-develop-mobile/ios/media-attachments/). Copy and paste the code provided in guide in respective files.	

- For Carousel Push you can follow the following [guide](https://docs.oracle.com/en/cloud/saas/marketing/responsys-develop-mobile/ios/carousel-push/). Copy and paste the code provided in guide in respective files.    

### Usage

The module can be accessed in JS code as follows,

```javascript
import PushIOManager from 'react-native-pushiomanager';
```


### Configure And Register

- Configure the SDK,

	```javascript
	PushIOManager.configure("pushio_config.json", (error, response) => {
	      
	});
	```
	
- Once the SDK is configured, register the app with Responsys,

	- For Android, 

		```javascript
		PushIOManager.registerApp(true, (error, response) => {
		
		});
		```
		
	- For iOS,

		```javascript
		PushIOManager.registerForAllRemoteNotificationTypes((error, response) => {
		
			PushIOManager.registerApp(true, (error, response) => {
                    
          });  
		});
		```
        
        - Use Platform check to detect the platform.        

            ```javascript
            import { Platform } from 'react-native';

            if (Platform.OS === 'android') {
                PushIOManager.registerApp(true, (error, response) => {
                
                });
            } else {
                PushIOManager.registerForAllRemoteNotificationTypes((error, response) => {
                
                    PushIOManager.registerApp(true, (error, response) => {
                            
                  });  
                });
            }
            ```


### User Identification

- Associate an app installation with a user (usually after login),

	```javascript
	PushIOManager.registerUserId("userID");
	```
	
- When the user logs out,

	```javascript
	PushIOManager.unregisterUserId();
	```
	

### Engagements And Conversion

User actions can be attributed to a push notification using,

```javascript
PushIOManager.trackEngagement(3, properties, (error, response) => {
	      
});
```

### In-App Messages

In-App Message (IAM) are displayed in a popup window via system-defined triggers like `$ExplicitAppOpen` or custom triggers. IAM that use system-defined triggers are displayed automatically.

IAM can also be displayed on-demand using custom triggers.

- Your marketing team defines a custom trigger in Responsys system and shares the trigger-event name with you.
- Marketer launches the campaign and the IAM is delivered to the device via push or pull mechanism (depending on your Responsys Account settings)
- When you wish to display the IAM popup, use,

	```javascript
	PushIOManager.trackEvent("custom_event_name", properties);
	```


### Message Center

- Get the Message Center messages list using,

	```javascript
	PushIOManager.fetchMessagesForMessageCenter(messageCenterName, (error, response) => {
	
	});
	```
	
- If any message has a rich-content (HTML) then call,

	```javascript
	PushIOManager.fetchRichContentForMessage(messageID, (error, response) => {
	      // 'response' is the HTML content
	});
	```
	
	Remember to store these messages, since the SDK cache is purgeable.
	

### Geofences And Beacons

If your app is setup to monitor geofence and beacons, you can use the following APIs to record in Responsys when a user enters/exits a geofence/beacon zone.

```javascript
PushIOManager.onGeoRegionEntered(geoRegion, (error, response) => {});
PushIOManager.onGeoRegionExited(geoRegion, (error, response) => {});
PushIOManager.onBeaconRegionEntered(beaconRegion, (error, response) => {});
PushIOManager.onBeaconRegionExited(beaconRegion, (error, response) => {});
```


### Notification Preferences

Preferences are used to record user-choices for push notifications. The preferences should be [pre-defined in Responsys](https://docs.oracle.com/en/cloud/saas/marketing/responsys-develop-mobile/dev-console/app-design/#notification-preferences) before being used in your app.

- Declare the preference beforehand in the app,

	```javascript
	PushIOManager.declarePreference(key, label, preferenceType, (error, response) => {
	
	});
	```

- Once a preference is declared successfully, you may save the preference using,

	```javascript
	PushIOManager.setPreference(key, value, (error, success) => {
	
	});
	```
	
Do not use this as a persistent (key/value) store since this data is purgeable.


## Support

If you have access to My Oracle Support, please raise a request [here](http://support.oracle.com/), otherwise open an issue in this repository. 


## License

Copyright (c) 2020 Oracle and/or its affiliates and released under the Universal Permissive License (UPL), Version 1.0.

Oracle and Java are registered trademarks of Oracle and/or its affiliates. Other names may be trademarks of their respective owners.
