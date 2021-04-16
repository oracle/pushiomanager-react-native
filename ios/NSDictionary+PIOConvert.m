/**
* Copyright © 2020, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/


#import "NSDictionary+PIOConvert.h"
#import <React/RCTConvert.h>

@implementation NSDictionary(PIOConvert)

- (PIOGeoRegion *)geoRegion {
    NSString *geofenceId = [RCTConvert NSString:self[@"geofenceId"]];
    NSString *geofenceName = [RCTConvert NSString:self[@"geofenceName"]];
    double speed = [RCTConvert double:self[@"speed"]];
    double bearing = [RCTConvert double:self[@"bearing"]];
    NSString *zoneId = [RCTConvert NSString:self[@"zoneId"]];
    NSString *zoneName = [RCTConvert NSString:self[@"zoneName"]];
    NSString *source = [RCTConvert NSString:self[@"source"]];
    NSInteger dwellTime = [RCTConvert NSInteger:self[@"dwellTime"]];
    NSDictionary *extra = [RCTConvert NSDictionary:self[@"extra"]];

    PIOGeoRegion *geoRegion = [[PIOGeoRegion alloc] initWithGeofenceId:geofenceId geofenceName:geofenceName speed:speed bearing:bearing source:source zoneId:zoneId zoneName:zoneName dwellTime:dwellTime extra:extra];
    
    return geoRegion;
}

- (PIOBeaconRegion *)beaconRegion {
    NSString *iBeaconUUID = [RCTConvert NSString:self[@"iBeaconUUID"]];
    NSInteger iBeaconMajor = [RCTConvert NSInteger:self[@"iBeaconMajor"]];
    NSInteger iBeaconMinor = [RCTConvert NSInteger:self[@"iBeaconMinor"]];
    NSString *beaconId = [RCTConvert NSString:self[@"beaconId"]];
    NSString *beaconName = [RCTConvert NSString:self[@"beaconName"]];
    NSString *beaconTag = [RCTConvert NSString:self[@"beaconTag"]];
    NSString *proximity = [RCTConvert NSString:self[@"proximity"]];
    NSString *zoneId = [RCTConvert NSString:self[@"zoneId"]];
    NSString *zoneName = [RCTConvert NSString:self[@"zoneName"]];
    NSString *source = [RCTConvert NSString:self[@"source"]];
    NSInteger dwellTime = [RCTConvert NSInteger:self[@"dwellTime"]];
    NSDictionary *extra = [RCTConvert NSDictionary:self[@"extra"]];
    NSString *eddyStoneId1 = [RCTConvert NSString:self[@"eddyStoneId1"]];
    NSString *eddyStoneId2 = [RCTConvert NSString:self[@"eddyStoneId2"]];
    PIOBeaconRegion *beaconRegion = [[PIOBeaconRegion alloc] initWithiBeaconUUID:iBeaconUUID iBeaconMajor:iBeaconMajor iBeaconMinor:iBeaconMinor beaconId:beaconId beaconName:beaconName beaconTag:beaconTag proximity:proximity source:source zoneId:zoneId zoneName:zoneName dwellTime:dwellTime extra:extra];
    beaconRegion.eddyStoneId1 = eddyStoneId1;
    beaconRegion.eddyStoneId2 = eddyStoneId2;
    
    return beaconRegion;
}

- (PIOConversionEvent *)conversionEvent {
    NSString *orderId = [RCTConvert NSString:self[@"orderId"]];
    double orderTotal = [RCTConvert double:self[@"orderTotal"]];
    NSInteger orderQuantity = [RCTConvert NSInteger:self[@"orderQuantity"]];
    int conversionType  = [RCTConvert int:self[@"conversionType"]];
    NSDictionary *customProperties = [RCTConvert NSDictionary:self[@"customProperties"]];
    if (customProperties == nil) {
        customProperties = [NSDictionary dictionary];
    }
    PIOConversionEvent *event = [[PIOConversionEvent alloc] initWithOrderId:orderId orderTotal:orderTotal orderQuantity:orderQuantity conversionType:conversionType customProperties:customProperties];
    return event;
}

- (PIONotificationCategory *)notificationCategory {
    NSArray *oracleButtons = self[@"orcl_btns"];
    NSMutableArray *actions = [NSMutableArray new];
    for (NSDictionary *action in oracleButtons) {
        PIONotificationAction *newAction = [[PIONotificationAction alloc] initWithIdentifier:action[@"id"] title:action[@"label"] isDestructive:[action[@"action"] isEqualToString:@"DE"] isForeground:[action[@"action"] isEqualToString:@"FG"] isAuthenticationRequired:[action[@"action"] isEqualToString:@"AR"]];
        [actions addObject:newAction];
    }
    return [[PIONotificationCategory alloc] initWithIdentifier:self[@"orcl_category"] actions:actions];
}

+ (NSDictionary *)dictionaryFromPreference:(PIOPreference *)preference {
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    dictionary[@"key"] = preference.key;
    dictionary[@"value"] = preference.value;
    dictionary[@"label"] = preference.label;
      switch (preference.type) {
          case PIOPreferenceTypeString:
              dictionary[@"type"] = @"PIOPreferenceTypeString";
              break;
          case PIOPreferenceTypeBoolean:
              dictionary[@"type"] = @"PIOPreferenceTypeBoolean";
              break;
          case PIOPreferenceTypeNumeric:
              dictionary[@"type"] = @"PIOPreferenceTypeNumeric";
              break;
      }
    return dictionary;
}

- (NSString *)JSON {
    NSError *err;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:self options:0 error:&err];
    
    if(err != nil) {
        return nil;
    }
    
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
}

@end
