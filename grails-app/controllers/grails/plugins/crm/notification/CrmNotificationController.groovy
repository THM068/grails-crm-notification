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

import grails.converters.JSON
import grails.plugins.crm.core.TenantUtils

import javax.servlet.http.HttpServletResponse

/**
 * CRUD operations for CrmNotification.
 */
class CrmNotificationController {

    def crmSecurityService
    def crmNotificationService

    def delete(Long id) {
        def n = CrmNotification.get(id)
        if (!n) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        }
        if (n.tenantId != null && n.tenantId != TenantUtils.tenant) {
            crmSecurityService.alert(request, "client at tenant ${TenantUtils.tenant} tried to delete notification ${n.id} at tenant ${n.tenantId}")
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return
        }
        def username = crmSecurityService.currentUser?.username
        if (n.username != null && n.username != username) {
            crmSecurityService.alert(request, "${username} at tenant ${TenantUtils.tenant} tried to delete notification ${n.id} for user ${n.username}")
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return
        }
        def tombstone = n.toString()
        crmNotificationService.delete(n)

        def count = crmNotificationService.countUnreadNotifications(username, TenantUtils.tenant)
        def json = [success: true, count: count, message: "Deleted $tombstone"]
        render json as JSON
    }
}
