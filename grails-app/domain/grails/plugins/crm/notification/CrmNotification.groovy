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

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.apache.commons.lang.StringUtils

class CrmNotification {

    public static final int PRIORITY_LOW = -1
    public static final int PRIORITY_NORMAL = 0
    public static final int PRIORITY_HIGH = 1

    Date dateCreated
    Long tenantId
    String username
    String body
    int priority
    boolean read

    static constraints = {
        tenantId(nullable: true)
        username(maxSize: 80, blank: false)
        body(maxSize: 2000, blank: false)
    }

    static mapping = {
        sort 'dateCreated'
    }

    static transients = ['payload']

    Map<String, Serializable> getPayload() {
        body ? new JsonSlurper().parseText(body) : null
    }

    void setPayload(Map<String, Serializable> arg = [:]) {
        def json = new JsonBuilder(arg).toString()
        def max = CrmNotification.constraints.body.maxSize
        if (json.length() > max) {
            throw new IllegalArgumentException("event payload to big: ${json.length()} > $max (${StringUtils.abbreviate(json, 40)})")
        }
        body = json
    }

    String toString() {
        "${dateCreated?.format("yyyy-MM-dd HH:mm:ss")} $username@$tenantId".toString()
    }
}
