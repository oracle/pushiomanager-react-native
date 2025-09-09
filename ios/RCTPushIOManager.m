/**
* Copyright © 2024, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/


#import "RCTPushIOManager.h"
#import <CX_Mobile_SDK/PushIOManagerAll.h>
#import <CX_Mobile_SDK/ORACoreConfig.h>
#import <CX_Mobile_SDK/ORACoreConstants.h>
#import "NSDictionary+PIOConvert.h"
#import "NSArray+PIOConvert.h"
#import "RCTPushIOEventEmitter.h"

@interface RCTPushIOManager()<PIODeepLinkDelegate>
@end

@implementation RCTPushIOManager

static RCTPushIOManager *_sharedInstance = nil;

+ (instancetype)sharedInstance {
    return _sharedInstance;
}


- (instancetype)init {
    
    self = [super init];
    
    if(self) {
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            ORACoreConfig *config = [[ORACoreConfig alloc] init];
            [config setConfigValue:@"rsys" forKey:kORAModules];
        });
        
        if([[NSUserDefaults standardUserDefaults] boolForKey:@"PIO_setOpenURLListener"]) {
            [[PushIOManager sharedInstance] setDeeplinkDelegate:self];
        }
         _pendingEvents = [NSMutableArray array];
        _sharedInstance = self;      
    }
    
    return  self;
}

RCT_EXPORT_MODULE()
+(BOOL)requiresMainQueueSetup {
    return YES;
}

RCT_EXPORT_METHOD(configure:(NSString *)filename:(RCTResponseSenderBlock)callback) {
  [[PushIOManager sharedInstance] configureWithFileName:filename completionHandler:^(NSError *error, NSString *response) {
    callback(@[error.description?: [NSNull null], response ?: @"success"]);
  }];
}

RCT_EXPORT_METHOD(configureWithAPIKey:(NSString *)apiKey accountToken:(NSString *)accountToken conversionUrl:(NSString *)conversionUrl riAppId:(NSString *)riAppId accountName:(NSString *)accountName  completionHandler:(RCTResponseSenderBlock)callback) {
  [[PushIOManager sharedInstance] configureWithAPIKey:apiKey accountToken:accountToken conversionUrl:conversionUrl riAppId:riAppId accountName:accountName completionHandler:^(NSError *error, NSString *response) {
    callback(@[error.description?: [NSNull null], response ?: @"success"]);
  }];
}


RCT_EXPORT_METHOD(registerForAllRemoteNotificationTypes) {
    [[NSNotificationCenter defaultCenter] postNotificationName:@"registerForAllRemoteNotificationTypes" object:nil];
}

RCT_EXPORT_METHOD(registerForAllRemoteNotificationTypesWithCategories:(NSArray *)categories completionHandler:(RCTResponseSenderBlock)callback) {
  [[PushIOManager sharedInstance] registerForAllRemoteNotificationTypesWithCategories:[categories notificationCategoryArray] completionHandler:^(NSError *error, NSString *response) {
    callback(@[error.description?: [NSNull null], response ?: @"success"]);
  }];
}

RCT_EXPORT_METHOD(registerForNotificationAuthorizations:(int)authOptions categories:(NSArray *)categories completionHandler:(RCTResponseSenderBlock)callback) {
    [[PushIOManager sharedInstance] registerForNotificationAuthorizations:authOptions categories:[categories notificationCategoryArray] completionHandler:^(NSError *error, NSString *response) {
        callback(@[error.description?: [NSNull null], response ?: @"success"]);

    }];
}

RCT_EXPORT_METHOD(registerApp:(BOOL)userLocation completionHandler:(RCTResponseSenderBlock)callback) {
    [[PushIOManager sharedInstance] registerApp:nil useLocation:userLocation completionHandler:^(NSError *error, NSString *response) {
        callback(@[error.description?: [NSNull null], response ?: @"success"]);
    }];
}
    
RCT_EXPORT_METHOD(unregisterApp:(RCTResponseSenderBlock)callback) {
  [[PushIOManager sharedInstance] unregisterApp:nil completionHandler:^(NSError *error, NSString *response) {
    callback(@[error.description?: [NSNull null], response ?: @"success"]);
  }];
}

RCT_EXPORT_METHOD(trackEngagement:(NSInteger)metric withProperties:(NSDictionary *)properties
                  completionHandler:(RCTResponseSenderBlock)callback) {
  [[PushIOManager sharedInstance] trackEngagementMetric:(int)metric withProperties:properties completionHandler:^(NSError *error, NSString *response) {
    callback(@[error.description?: [NSNull null], response ?: @"success"]);
  }];
}

RCT_EXPORT_METHOD(resetEngagementContext) {
  [[PushIOManager sharedInstance] resetEngagementContext];
}

RCT_EXPORT_METHOD(getEngagementMaxAge:(RCTResponseSenderBlock)callback) {
  NSNumber *engagementAge = @([[PushIOManager sharedInstance] getEngagementMaxAge]);
  callback(@[[NSNull null], engagementAge]);
}

RCT_EXPORT_METHOD(getEngagementTimestamp:(RCTResponseSenderBlock)callback) {
  NSString *engagementTimeStamp = [[PushIOManager sharedInstance] getEngagementTimeStamp];
  callback(@[[NSNull null], (engagementTimeStamp == nil ? [NSNull null] : engagementTimeStamp)]);
}

RCT_EXPORT_METHOD(getAPIKey:(RCTResponseSenderBlock)callback) {
    NSString *apiKey = [[PushIOManager sharedInstance] getAPIKey];
    callback(@[[NSNull null], (apiKey == nil ? [NSNull null] : apiKey)]);
}

RCT_EXPORT_METHOD(getAccountToken:(RCTResponseSenderBlock)callback) {
    NSString *accountToken = [[PushIOManager sharedInstance] getAccountToken];
    callback(@[[NSNull null], (accountToken == nil ? [NSNull null] : accountToken)]);
}

RCT_EXPORT_METHOD(getDeviceID:(RCTResponseSenderBlock)callback) {
    NSString *deviceID = [[PushIOManager sharedInstance] getDeviceID];
    callback(@[[NSNull null], (deviceID == nil ? [NSNull null] : deviceID)]);
}

RCT_EXPORT_METHOD(getPreferences:(RCTResponseSenderBlock)callback) {
    NSArray *preferences = [[[PushIOManager sharedInstance] getPreferences] preferencesDictionary];
    callback(@[[NSNull null], (preferences == nil ? [NSNull null] : preferences)]);
}

RCT_EXPORT_METHOD(getPreference:(NSString *)key completionHandler:(RCTResponseSenderBlock)callback) {
    PIOPreference *preference = [[PushIOManager sharedInstance] getPreference:key];
    NSDictionary *dictionary = [NSDictionary dictionaryFromPreference:preference];
    callback(@[[NSNull null], (dictionary == nil ? [NSNull null] : dictionary)]);
}


RCT_EXPORT_METHOD(trackEvent:(NSString *)eventName properties:(NSDictionary *)properties) {
  [[PushIOManager sharedInstance] trackEvent:eventName properties:properties];
}

RCT_EXPORT_METHOD(trackMessageCenterOpenEngagement:(NSString *)messageId) {
  [[PushIOManager sharedInstance] trackMessageCenterOpenEngagement:messageId];
}

RCT_EXPORT_METHOD(trackMessageCenterDisplayEngagement:(NSString *)messageId) {
  [[PushIOManager sharedInstance] trackMessageCenterDisplayEngagement:messageId];
}

RCT_EXPORT_METHOD(messageCenterViewWillAppear) {
  [[PushIOManager sharedInstance] messageCenterViewWillAppear];
}

RCT_EXPORT_METHOD(messageCenterViewWillDisappear) {
  [[PushIOManager sharedInstance] messageCenterViewWillDisappear];
}

RCT_EXPORT_METHOD(declarePreference:(NSString *)key label:(NSString *)label type:(NSString *)type completionHandler:(RCTResponseSenderBlock)callback) {
  NSError *error = nil;
    if (type == (id)[NSNull null] || type.length == 0) {
        callback(@[@"Preference type is NULL. Should be \"STRING\" or \"NUMBER\" or \"BOOLEAN\""]);
    }else{
        [[PushIOManager sharedInstance] declarePreference:key label:label type:([type isEqualToString:@"STRING"] ? PIOPreferenceTypeString : ([type isEqualToString:@"NUMBER"] ? PIOPreferenceTypeNumeric : PIOPreferenceTypeBoolean)) error:&error];
        callback(@[error.description?: [NSNull null],@"success"]);
    }
  
}

RCT_EXPORT_METHOD(setBooleanPreference:(NSString *)key forValue:(BOOL)value completionHandler:(RCTResponseSenderBlock)callback) {
    callback(@[[NSNull null], @([[PushIOManager sharedInstance] setBoolPreference:value forKey:key])]);
}

RCT_EXPORT_METHOD(setStringPreference:(NSString *)key  forValue:(NSString *)value completionHandler:(RCTResponseSenderBlock)callback) {
    callback(@[[NSNull null], @([[PushIOManager sharedInstance] setStringPreference:value forKey:key])]);
}

RCT_EXPORT_METHOD(setNumberPreference:(NSString *)key  forValue:(nonnull NSNumber *)value completionHandler:(RCTResponseSenderBlock)callback) {
    callback(@[[NSNull null], @([[PushIOManager sharedInstance] setNumberPreference:value forKey:key])]);
}

RCT_EXPORT_METHOD(removePreference:(NSString *)key) {
    NSError *error = nil;
  [[PushIOManager sharedInstance] removePreference:key error:&error];
}

RCT_EXPORT_METHOD(clearAllPreferences) {
  [[PushIOManager sharedInstance] clearAllPreferences];
}

RCT_EXPORT_METHOD(setMessageCenterEnabled:(BOOL)enableMessageCenter) {
  [[PushIOManager sharedInstance] setMessageCenterEnabled:enableMessageCenter];
}

RCT_EXPORT_METHOD(isMessageCenterEnabled:(RCTResponseSenderBlock)callback) {
  callback(@[[NSNull null], @([[PushIOManager sharedInstance] isMessageCenterEnabled])]);
}

RCT_EXPORT_METHOD(fetchMessagesForMessageCenter:(NSString *)messageCenter completionHandler:(RCTResponseSenderBlock)callback) {
    NSMutableDictionary *responseDictionary = [NSMutableDictionary dictionary];
    responseDictionary[@"messageCenter"] = messageCenter;
    [[PushIOManager sharedInstance] fetchMessagesForMessageCenter:messageCenter CompletionHandler:^(NSError *error, NSArray *messages) {
        responseDictionary[@"messages"] = [messages messageDictionary];
        callback(@[error.description?: [NSNull null], responseDictionary]);
  }];
}


RCT_EXPORT_METHOD(fetchRichContentForMessage:(NSString *)messageID completionHandler:(RCTResponseSenderBlock)callback) {
  [[PushIOManager sharedInstance] fetchRichContentForMessage:messageID CompletionHandler:^(NSError *error, NSString *messageID, NSString *richContent) {
      NSMutableDictionary *responseDictionary = [NSMutableDictionary dictionary];
      responseDictionary[@"richContent"] = richContent;
      responseDictionary[@"messageID"] = messageID;
    callback(@[error.description?: [NSNull null], responseDictionary]);
  }];
}

RCT_EXPORT_METHOD(setBadgeCount:(NSInteger)badgeCount completionHandler:(RCTResponseSenderBlock)callback) {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[PushIOManager sharedInstance] setBadgeCount:badgeCount completionHandler:^(NSError *error, NSString *response) {
            callback(@[error.description?: [NSNull null], response ?: @"success"]);
        }];
    });
    
}

RCT_EXPORT_METHOD(resetBadgeCount:(RCTResponseSenderBlock)callback) {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[PushIOManager sharedInstance] resetBadgeCountWithCompletionHandler:^(NSError *error, NSString *response) {
            callback(@[error.description?: [NSNull null], response ?: @"success"]);
        }];
    });
}

RCT_EXPORT_METHOD(getBadgeCount:(RCTResponseSenderBlock)callback) {
    dispatch_async(dispatch_get_main_queue(), ^{
        callback(@[[NSNull null], @([[PushIOManager sharedInstance] getBadgeCount] ?: 0)]);
    });
}

RCT_EXPORT_METHOD(clearInAppMessages) {
  [[PushIOManager sharedInstance] clearInAppMessages];
}

RCT_EXPORT_METHOD(clearMessageCenterMessages) {
  [[PushIOManager sharedInstance] clearMessageCenterMessages];
}

RCT_EXPORT_METHOD(isSDKConfigured:(RCTResponseSenderBlock)callback) {
  callback(@[[NSNull null], @([[PushIOManager sharedInstance] isSDKConfigured])]);
}


RCT_EXPORT_METHOD(setInAppMessageFetchEnabled:(BOOL)enableInAppMessageFetch) {
  [[PushIOManager sharedInstance] setInAppMessageFetchEnabled:enableInAppMessageFetch];
}

RCT_EXPORT_METHOD(setLoggingEnabled:(BOOL)enable) {
  [[PushIOManager sharedInstance] setLoggingEnabled:enable];
}

RCT_EXPORT_METHOD(setLogLevel:(NSInteger)logLevel) {
  [[PushIOManager sharedInstance] setLogLevel:logLevel];
}

RCT_EXPORT_METHOD(isLoggingEnabled:(RCTResponseSenderBlock)callback) {
  callback(@[[NSNull null], @([[PushIOManager sharedInstance] isLoggingEnabled])]);
}

RCT_EXPORT_METHOD(registerUserId:(NSString *)userID) {
  [[PushIOManager sharedInstance] registerUserID:userID];
}

RCT_EXPORT_METHOD(getUserId:(RCTResponseSenderBlock)callback) {
    NSString *userId = [[PushIOManager sharedInstance] getUserID];
    callback(@[[NSNull null], (userId == nil ? [NSNull null] : userId)]);
}

RCT_EXPORT_METHOD(unregisterUserId) {
  [[PushIOManager sharedInstance] registerUserID:nil];
}

RCT_EXPORT_METHOD(frameworkVersion:(RCTResponseSenderBlock)callback) {
  callback(@[[NSNull null], [[PushIOManager sharedInstance] frameworkVersion]]);
}

RCT_EXPORT_METHOD(setExternalDeviceTrackingID:(NSString *)externalDeviceTrackingID) {
  [[PushIOManager sharedInstance] setExternalDeviceTrackingID:externalDeviceTrackingID];
}

RCT_EXPORT_METHOD(externalDeviceTrackingID:(RCTResponseSenderBlock)callback) {
  NSString *externalDeviceTrackingID = [[PushIOManager sharedInstance] externalDeviceTrackingID];
  callback(@[[NSNull null], (externalDeviceTrackingID == nil ? [NSNull null] : externalDeviceTrackingID)]);
}


RCT_EXPORT_METHOD(setAdvertisingIdentifier:(NSString *)advertisingIdentifier) {
  [[PushIOManager sharedInstance] setAdvertisingIdentifier:advertisingIdentifier];
}

RCT_EXPORT_METHOD(advertisingIdentifier:(RCTResponseSenderBlock)callback) {
    callback(@[[NSNull null], ([[PushIOManager sharedInstance] advertisingIdentifier] ?: [NSNull null])]);
}


RCT_EXPORT_METHOD(setExecuteRsysWebURL:(BOOL)executeRsysWebURL) {
  [[PushIOManager sharedInstance] setExecuteRsysWebURL:executeRsysWebURL];
}

RCT_EXPORT_METHOD(executeRsysWebURL:(RCTResponseSenderBlock)callback) {
  callback(@[[NSNull null], @([[PushIOManager sharedInstance] executeRsysWebURL])]);
}

RCT_EXPORT_METHOD(setConfigType:(NSInteger)configType) {
  [[PushIOManager sharedInstance] setConfigType:configType];
}

RCT_EXPORT_METHOD(configType:(RCTResponseSenderBlock)callback) {
  callback(@[[NSNull null], @([[PushIOManager sharedInstance] configType])]);
}

RCT_EXPORT_METHOD(resetAllData) {
  [[PushIOManager sharedInstance] resetAllData];
}

RCT_EXPORT_METHOD(isResponsysPayload:message completionHandler:(RCTResponseSenderBlock)callback) {
  callback(@[[NSNull null], @([[PushIOManager sharedInstance] isResponsysPayload:message])]);
}

RCT_EXPORT_METHOD(didEnterGeoRegion:(NSDictionary *)region  completionHandler:(RCTResponseSenderBlock)callback) {
    PIOGeoRegion *geoRegion = [region geoRegion];
    NSMutableDictionary *responseDictionary = [NSMutableDictionary dictionary];
    responseDictionary[@"regionType"] = @"GEOFENCE_ENTRY";
    responseDictionary[@"regionID"] = geoRegion.geofenceId;

    [[PushIOManager sharedInstance] didEnterGeoRegion:geoRegion completionHandler:^(NSError *error, NSString *response) {
        callback(@[error.description?: [NSNull null], responseDictionary]);
    }];
}

RCT_EXPORT_METHOD(didExitGeoRegion:(NSDictionary *)region  completionHandler:(RCTResponseSenderBlock)callback) {
    PIOGeoRegion *geoRegion = [region geoRegion];
    NSMutableDictionary *responseDictionary = [NSMutableDictionary dictionary];
    responseDictionary[@"regionType"] = @"GEOFENCE_EXIT";
    responseDictionary[@"regionID"] = geoRegion.geofenceId;

    [[PushIOManager sharedInstance] didExitGeoRegion:geoRegion completionHandler:^(NSError *error, NSString *response) {
        callback(@[error.description?: [NSNull null], responseDictionary]);
    }];
}

RCT_EXPORT_METHOD(didEnterBeaconRegion:(NSDictionary *)region  completionHandler:(RCTResponseSenderBlock)callback) {

    PIOBeaconRegion *beaconRegion = [region beaconRegion];
    NSMutableDictionary *responseDictionary = [NSMutableDictionary dictionary];
    responseDictionary[@"regionType"] = @"BEACON_ENTRY";
    responseDictionary[@"regionID"] = beaconRegion.beaconId;

    [[PushIOManager sharedInstance] didEnterBeaconRegion:beaconRegion completionHandler:^(NSError *error, NSString *response) {
        callback(@[error.description?: [NSNull null], responseDictionary]);
    }];
}

RCT_EXPORT_METHOD(didExitBeaconRegion:(NSDictionary *)region  completionHandler:(RCTResponseSenderBlock)callback) {
    PIOBeaconRegion *beaconRegion = [region beaconRegion];
    NSMutableDictionary *responseDictionary = [NSMutableDictionary dictionary];
    responseDictionary[@"regionType"] = @"BEACON_EXIT";
    responseDictionary[@"regionID"] = beaconRegion.beaconId;

    [[PushIOManager sharedInstance] didExitBeaconRegion:[region beaconRegion] completionHandler:^(NSError *error, NSString *response) {
        callback(@[error.description?: [NSNull null], responseDictionary]);
    }];
}

RCT_EXPORT_METHOD(setDelayRichPushDisplay:(BOOL)delayRichPush) {
  [[PushIOManager sharedInstance] setDelayRichPushDisplay:delayRichPush];
}

RCT_EXPORT_METHOD(showRichPushMessage) {
  [[PushIOManager sharedInstance] showRichPushMessage];
}

RCT_EXPORT_METHOD(isRichPushDelaySet:(RCTResponseSenderBlock)callback) {
  callback(@[[NSNull null], @([[PushIOManager sharedInstance] isRichPushDelaySet])]);
}


RCT_EXPORT_METHOD(setOpenURLListener:(BOOL)isSet) {
    if (isSet) {
         [[PushIOManager sharedInstance] setDeeplinkDelegate:self];
    } else {
        [[PushIOManager sharedInstance] setDeeplinkDelegate:nil];
    }
    [[NSUserDefaults standardUserDefaults] setBool:isSet forKey:@"PIO_setOpenURLListener"];
}

RCT_EXPORT_METHOD(trackConversionEvent:(NSDictionary *)event
                  completionHandler:(RCTResponseSenderBlock)callback) {
  [[PushIOManager sharedInstance] trackConversionEvent:[event conversionEvent]
                                     completionHandler:^(NSError *error, NSString *response) {
      callback(@[error.description?: [NSNull null], response ?: @"success"]);
  }];
}

RCT_EXPORT_METHOD(setDelayRegistration:(BOOL)delayRegistration) {
  [[PushIOManager sharedInstance] setDelayRegistration:delayRegistration];
}

RCT_EXPORT_METHOD(isDelayRegistration:(RCTResponseSenderBlock)callback) {
    callback(@[[NSNull null], @([[PushIOManager sharedInstance] delayRegistration])]);
}

- (BOOL)handleOpenURL:(NSURL *)url {
    
    if([RCTPushIOEventEmitter sharedInstance].hasListeners){    
        [[NSNotificationCenter defaultCenter] postNotificationName:@"PIOHandleOpenURL" object:nil userInfo:@{@"url": [url absoluteString]}];
    }else{     
        NSDictionary *event = @{@"name": @"PIOHandleOpenURL",
                                @"body": @{@"url": [url absoluteString]}};
        
        NSLog(@"[RCTPushIOManager] Queued Event : %@",event);    
        [self.pendingEvents addObject:event];
    }    
    return true;
}

RCT_EXPORT_METHOD(setInAppMessageBannerHeight:(CGFloat )height completionHandler:(RCTResponseSenderBlock)callback) {
    BOOL hasBannerHeightSet = [[PushIOManager sharedInstance] setInAppMessageBannerHeight:height];
    if(callback != nil){
        callback(@[[NSNull null], @(hasBannerHeightSet)]);
    }
}

RCT_EXPORT_METHOD(getInAppMessageBannerHeight:(RCTResponseSenderBlock)callback) {
    callback(@[[NSNull null], @([[PushIOManager sharedInstance] getInAppMessageBannerHeight])]);
}

RCT_EXPORT_METHOD(setStatusBarHiddenForIAMBannerInterstitial:(BOOL)hideStatusBar){
    [[PushIOManager sharedInstance] setStatusBarHiddenForIAMBannerInterstitial:hideStatusBar];
}

RCT_EXPORT_METHOD(isStatusBarHiddenForIAMBannerInterstitial:(RCTResponseSenderBlock)callback) {
    callback(@[[NSNull null], @([[PushIOManager sharedInstance] isStatusBarHiddenForIAMBannerInterstitial])]);
}

RCT_EXPORT_METHOD(setInAppCustomCloseButton:(NSDictionary *)closeButton) {
    NSDictionary *customCloseBluttonInfo = (NSDictionary *)closeButton;
    if (customCloseBluttonInfo == (id)[NSNull null]) {
        customCloseBluttonInfo = nil;
    }
    UIButton *closeButtonui = [customCloseBluttonInfo customCloseButton];
    if(closeButtonui != nil){
      [[PushIOManager sharedInstance] setInAppMessageCloseButton:closeButtonui];
    }
}

@end

