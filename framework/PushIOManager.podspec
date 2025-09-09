Pod::Spec.new do |s|
	s.version                 = "7.0.1"
   	s.name                    = "PushIOManager"
   	s.summary                 = "Responsys iOS SDK"
   	s.documentation_url       = ""
   	s.homepage                = "https://github.com/pushio/PushIOManager_iOS"
   	s.author                  = "Oracle"
        s.license                 = ""
   	s.source                  = { :git => "https://github.com/pushio/PushIOManager_iOS", :tag => s.version.to_s }

   	s.module_name             = "PushIOManager"
   	s.ios.deployment_target   = "12.0"
   	s.requires_arc            =  true

 	s.vendored_frameworks 	  = 'framework/CX_Mobile_SDK.xcframework','framework/OracleCXLocationSDK.xcframework'
 	s.preserve_paths 	      = "framework/CX_Mobile_SDK.xcframework/**/*","framework/OracleCXLocationSDK.xcframework/**/*"
   	
   	s.libraries               = 'sqlite3'
   	s.frameworks              = 'UserNotifications', 'CoreLocation', 'Foundation', 'UIKit'
   	s.ios.frameworks          = 'WebKit'
end
