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

import grails.test.GroovyPagesTestCase
import org.grails.plugin.platform.events.EventMessage

/**
 * TagLib tests.
 */
class CrmNotificationTagLibTests extends GroovyPagesTestCase {

    def grailsEventsPublisher

    def testTaglib() {

        grailsEventsPublisher.event(new EventMessage("notify", [tenant: 42L, username: "test", msg: "Hello World"], "crm", true))
        Thread.sleep(2000L)

        def template = '<crm:eachNotification username="test" tenant="42">\${it.payload.msg}</crm:eachNotification>'
        assert applyTemplate(template) == "Hello World"
    }
}
