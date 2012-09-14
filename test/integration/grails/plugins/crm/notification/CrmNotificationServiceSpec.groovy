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

import org.grails.plugin.platform.events.EventMessage

/**
 * Test specification for CrmNotificationService.
 */
class CrmNotificationServiceSpec extends grails.plugin.spock.IntegrationSpec {

    def grailsEventsPublisher
    def crmNotificationService

    def "send notification event and make sure it got persisted"() {

        when: "notification is sent"
        grailsEventsPublisher.event(new EventMessage("notify", [tenant: 42L, username: "test", msg: "Hello World"], "crm", true))
        Thread.sleep(2000L)

        then: "event consumed by crmNotificationService"
        crmNotificationService.getNotifications('test', 42L).size() == 1
    }
}
