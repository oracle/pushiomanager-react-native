/**
* Copyright © 2024, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/


#import <React/RCTBridgeModule.h>

@interface RCTPushIOManager : NSObject <RCTBridgeModule>
+ (instancetype)sharedInstance;
@property (nonatomic, strong) NSMutableArray<NSDictionary *> *pendingEvents;
@end
