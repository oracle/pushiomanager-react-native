/**
* Copyright © 2024, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/

#import "RCTPushIOEventEmitter.h"
#import <Foundation/Foundation.h>
#import <CX_Mobile_SDK/PushIOManagerAll.h>
#import "RCTPushIOManager.h"

@interface RCTPushIOEventEmitter () {
    BOOL openUrlListenerAdded;
}
@property (strong,nonatomic)NSDictionary *deeplinkUserInfo;
@end

@implementation RCTPushIOEventEmitter {
}

static RCTPushIOEventEmitter *_sharedInstance = nil;

+ (instancetype)sharedInstance {
    return _sharedInstance;
}

- (instancetype)init {
    
    self = [super init];
    if(self) {
         _sharedInstance = self;
    }
    return self;
}




RCT_EXPORT_MODULE();

+(BOOL)requiresMainQueueSetup {
    return NO;
}

-(void)startObserving {
    _hasListeners = YES;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(resolvedURL:) name:PIORsysWebURLResolvedNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleOpenURL:) name:@"PIOHandleOpenURL" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(registerForAllRemoteNotifications) name:@"registerForAllRemoteNotificationTypes" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleMessageCenterUpdate:) name:PIOMessageCenterUpdateNotification object:nil];
}

- (void)registerForAllRemoteNotifications {
    
    [[PushIOManager sharedInstance] registerForAllRemoteNotificationTypes:^(NSError *error, NSString *response) {
        if ([self bridge] == nil) {
            return;
        }
        if (self->_hasListeners ) {
              NSMutableDictionary *result = [NSMutableDictionary dictionary];
              result[@"error"] = error.description?: [NSNull null] ;
              result[@"response"] = response?: @"success";
            [self sendEventWithName:@"registerForAllRemoteNotificationTypes" body:result];

        }
    }];
}

-(void)stopObserving {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"registerForAllRemoteNotificationTypes" object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:PIORsysWebURLResolvedNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"PIOHandleOpenURL" object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:PIOMessageCenterUpdateNotification object:nil];

    _hasListeners = NO;
}

- (void)resolvedURL:(NSNotification *)notification {
  if (_hasListeners) {
    [self sendEventWithName:PIORsysWebURLResolvedNotification body:notification.userInfo];
  }
}

- (void)handleOpenURL:(NSNotification *)notification {

    if (openUrlListenerAdded == true) {
        [self sendEventWithName:@"PIOHandleOpenURL" body:notification.userInfo];
    } else {
        self.deeplinkUserInfo =  notification.userInfo;
    }
}

- (NSArray<NSString *> *)supportedEvents {
  return @[PIORsysWebURLResolvedNotification, @"PIOHandleOpenURL", @"registerForAllRemoteNotificationTypes",PIOMessageCenterUpdateNotification];
}

- (void)handleMessageCenterUpdate:(NSNotification *)notification {
    if (_hasListeners) {
        NSArray *messageCenters =  (NSArray *)[notification object];
        NSString *messageCenter = [messageCenters componentsJoinedByString:@","];
        [self sendEventWithName:PIOMessageCenterUpdateNotification body:messageCenter];
    }
}

-(void)flushStoredEvents {   
    NSMutableArray<NSDictionary *> *pendingEvents = [[RCTPushIOManager sharedInstance].pendingEvents copy];
    
    if(pendingEvents.count == 0){
        return;
    }
    for (NSDictionary *event in pendingEvents) {
        [self sendEventWithName:event[@"name"] body:event[@"body"]];
    }  
    [[RCTPushIOManager sharedInstance].pendingEvents removeAllObjects];
}

-(void) addListener:(NSString *)eventName {
    
    [super addListener:eventName];
    
    if([eventName isEqualToString:@"PIOHandleOpenURL"]) {
        openUrlListenerAdded = true;

        [self flushStoredEvents];

        if(self.deeplinkUserInfo != nil) {
            [self sendEventWithName:@"PIOHandleOpenURL" body:self.deeplinkUserInfo];
        }
    }
}



@end

