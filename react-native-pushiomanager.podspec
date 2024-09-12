require "json"
package = JSON.parse(File.read(File.join(__dir__, "package.json")))
Pod::Spec.new do |s|
  s.name         = "react-native-pushiomanager"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = "https://github.com/oracle/pushiomanager-react-native"
  s.license      = "UPL-1.0"
  # s.license    = { :type => "UPL-1.0", :file => "FILE_LICENSE" }
  s.authors      = { "Responsys" => "support@oracle.com" }
  s.platforms    = { :ios => "12.0" }
  s.source       = { :path => '.' }
  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true
  s.preserve_paths = 'CX_Mobile_SDK.xcframework'
  s.xcconfig = { 'OTHER_LDFLAGS' => '-framework CX_Mobile_SDK -ObjC' }
  s.vendored_frameworks = 'framework/CX_Mobile_SDK.xcframework','framework/OracleCXLocationSDK.xcframework'
  s.preserve_paths 	  = "framework/CX_Mobile_SDK.xcframework/**/*","framework/OracleCXLocationSDK.xcframework/**/*"
  s.libraries      = 'sqlite3'
  s.frameworks              = 'UserNotifications', 'CoreLocation', 'Foundation', 'UIKit'
  s.ios.frameworks          = 'WebKit'
  s.dependency "React"
end
