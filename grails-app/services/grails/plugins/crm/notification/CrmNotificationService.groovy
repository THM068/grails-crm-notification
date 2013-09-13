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

import grails.events.Listener
import groovy.transform.CompileStatic

/**
 * Grails CRM Notification Center.
 */
class CrmNotificationService {

    @Listener(namespace = "crm", topic = "notify")
    def notify(event) {
        def subject = event.remove('subject')
        if (! subject) {
            subject = event.remove('title')
            if (! subject) {
                subject = "<no subject>"
            }
        }
        new CrmNotification(tenantId: event.remove('tenant'), username: event.remove('username'),
                subject: subject, priority: event.remove('priority') ?: 0, payload: event).save(failOnError: true)
    }

    List<CrmNotification> getNotifications(final String username, Long tenant = null, Map orderParams = [:]) {
        CrmNotification.createCriteria().list(orderParams) {
            eq('username', username)
            if (tenant != null) {
                eq('tenantId', tenant)
            } else {
                isNull('tenantId')
            }
        }
    }

    List<CrmNotification> getUnreadNotifications(final String username, Long tenant = null, Map orderParams = [:]) {
        CrmNotification.createCriteria().list(orderParams) {
            eq('username', username)
            if (tenant != null) {
                eq('tenantId', tenant)
            } else {
                isNull('tenantId')
            }
            eq('viewed', false)
        }
    }

    int countUnreadNotifications(final String username, Long tenant = null) {
        CrmNotification.createCriteria().count() {
            eq('username', username)
            if (tenant != null) {
                eq('tenantId', tenant)
            } else {
                isNull('tenantId')
            }
            eq('viewed', false)
        }
    }

    @CompileStatic
    void markAsRead(final CrmNotification arg) {
        arg.viewed = true
        arg.save()
    }

    @CompileStatic
    void markAsUnRead(final CrmNotification arg) {
        arg.viewed = false
        arg.save()
    }

    @CompileStatic
    void delete(final CrmNotification arg) {
        arg.delete()
    }
}
