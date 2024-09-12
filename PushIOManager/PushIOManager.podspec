Pod::Spec.new do |s|
	s.version                 = "7.0.0"
   	s.name                    = "PushIOManager"
   	s.summary                 = "CX iOS SDK"
   	s.documentation_url       = ""
   	s.homepage                = "https://www.oracle.com/downloads/applications/cx/responsys-mobile-sdk.html"
   	s.author                  = "Oracle"
    s.license                 = ""
   	s.source                  = { :http => "https://www.oracle.com/downloads/applications/cx/responsys-mobile-sdk.html", :tag => s.version.to_s }

   	s.module_name             = "PushIOManager"
   	s.ios.deployment_target   = "12.0"
   	s.requires_arc            =  true

   	s.vendored_frameworks 	  = 'framework/CX_Mobile_SDK.xcframework','framework/OracleCXLocationSDK.xcframework'
  	s.preserve_paths 	  	  = "framework/CX_Mobile_SDK.xcframework/**/*","framework/OracleCXLocationSDK.xcframework/**/*"
   	
   	s.libraries               = 'sqlite3'
   	s.frameworks              = 'UserNotifications', 'CoreLocation', 'Foundation', 'UIKit'
   	s.ios.frameworks          = 'WebKit'
end
