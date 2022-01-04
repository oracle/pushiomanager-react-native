/**
* Copyright © 2022, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/


#import "NSArray+PIOConvert.h"
#import "NSDictionary+PIOConvert.h"
#import <PushIOManager/PushIOManagerAll.h>

@implementation NSArray (PIOConvert)
- (NSArray *)messageDictionary {
  NSMutableArray *messages = [NSMutableArray array];
  for (PIOMCMessage *message in self) {
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    dictionary[@"messageID"] = message.messageID;
    dictionary[@"subject"] = message.subject;
    dictionary[@"message"] = message.message;
    dictionary[@"iconURL"] = message.iconURL;
    dictionary[@"messageCenterName"] = message.messageCenterName;
    dictionary[@"deeplinkURL"] = message.deeplinkURL;
    dictionary[@"richMessageHTML"] = message.richMessageHTML;
    dictionary[@"richMessageURL"] = message.richMessageURL;
    dictionary[@"sentTimestamp"] = [self dateToString:message.sentTimestamp];
    dictionary[@"expiryTimestamp"] = [self dateToString:message.expiryTimestamp];

    [messages addObject:dictionary];
  }
        
  return messages;
}

- (NSArray *)notificationCategoryArray {
  NSMutableArray *categories = [NSMutableArray array];
  for (NSDictionary *category in self) {
    [categories addObject:[category notificationCategory]];
  }
  return categories;
}

- (NSString *)dateToString:(NSDate *)date {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    NSLocale *enUSPOSIXLocale = [NSLocale localeWithLocaleIdentifier:@"en_US_POSIX"];
    [dateFormatter setLocale:enUSPOSIXLocale];
    [dateFormatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ssZZZZZ"];
    [dateFormatter setCalendar:[NSCalendar calendarWithIdentifier:NSCalendarIdentifierGregorian]];

    return [dateFormatter stringFromDate:date];
}



- (NSArray *)preferencesDictionary {
  NSMutableArray *preferences = [NSMutableArray array];
  for (PIOPreference *preference in self) {
    NSDictionary *dictionary = [NSDictionary dictionaryFromPreference:preference];
    [preferences addObject:dictionary];
  }
        
  return preferences;
}

@end
