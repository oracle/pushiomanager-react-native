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
}



// Will be called when this module's last listener is removed, or on dealloc.
-(void)stopObserving {
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
  return @[PIORsysWebURLResolvedNotification, @"PIOHandleOpenURL"];
}


@end

