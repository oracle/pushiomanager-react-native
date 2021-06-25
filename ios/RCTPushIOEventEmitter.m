/**
* Copyright © 2020, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/

#import "RCTPushIOEventEmitter.h"
#import <Foundation/Foundation.h>
#import <PushIOManager/PushIOManagerAll.h>

@implementation RCTPushIOEventEmitter {
  BOOL hasListeners;
}

RCT_EXPORT_MODULE();

-(void)startObserving {
    hasListeners = YES;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(resolvedURL:) name:PIORsysWebURLResolvedNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleOpenURL:) name:@"PIOHandleOpenURL" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(registerForAllRemoteNotifications) name:@"registerForAllRemoteNotificationTypes" object:nil];

}


- (void)registerForAllRemoteNotifications {
    
    [[PushIOManager sharedInstance] registerForAllRemoteNotificationTypes:^(NSError *error, NSString *response) {
        if ([self bridge] == nil) {
            return;
        }
        if (self->hasListeners ) {
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

    hasListeners = NO;
}

- (void)resolvedURL:(NSNotification *)notification {
  if (hasListeners) {
    [self sendEventWithName:PIORsysWebURLResolvedNotification body:notification.userInfo];
  }
}

- (void)handleOpenURL:(NSNotification *)notification {
    if (hasListeners) {
        [self sendEventWithName:@"PIOHandleOpenURL" body:notification.userInfo];
    }
}

- (NSArray<NSString *> *)supportedEvents {
  return @[PIORsysWebURLResolvedNotification, @"PIOHandleOpenURL", @"registerForAllRemoteNotificationTypes"];
}


@end

