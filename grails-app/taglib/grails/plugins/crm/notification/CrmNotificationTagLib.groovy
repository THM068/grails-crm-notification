/*
 * Copyright 2012 Goran Ehrsson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugins.crm.notification

class CrmNotificationTagLib {

    static namespace = "crm"

    def crmNotificationService
    def crmSecurityService

    def hasUnreadNotifications = {attrs, body ->
        def username = attrs.username ?: crmSecurityService.currentUser?.username
        if (!username) {
            return
        }
        def tenant = attrs.long('tenant')
        def count = crmNotificationService.countUnreadNotifications(username, tenant)
        if (count > 0) {
            out << body([count:count])
        }
    }

    def eachNotification = {attrs, body ->
        def username = attrs.username ?: crmSecurityService.currentUser?.username
        if (!username) {
            return
        }
        def tenant = attrs.long('tenant')
        def params = [offset: attrs.int('offset'), max: attrs.int('max') ?: 1000, sort: attrs.sort ?: 'dateCreated', order: attrs.order ?: 'desc']
        def result = crmNotificationService.getNotifications(username, tenant, params)
        int i = 0
        for (s in result) {
            def map = [(attrs.var ?: 'it'): s]
            if (attrs.status) {
                map[attrs.status] = i++
            }
            out << body(map)
        }
    }
}
