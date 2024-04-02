/**
* Copyright © 2024, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/


#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSArray (PIOConvert)
- (NSArray *)messageDictionary;
- (NSArray *)preferencesDictionary;
- (NSArray *)notificationCategoryArray;
- (NSString *)JSON;
@end

NS_ASSUME_NONNULL_END
