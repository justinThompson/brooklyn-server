/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.brooklyn.rest.util;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.apache.brooklyn.api.mgmt.ManagementContext;
import org.apache.brooklyn.core.server.BrooklynServiceAttributes;

import com.google.common.annotations.VisibleForTesting;

@Provider
// Needed by tests in rest-resources module and by main code in rest-server
public class ManagementContextProvider implements ContextResolver<ManagementContext> {
    @Context
    private ServletContext context;

    private ManagementContext mgmt;

    public ManagementContextProvider() {
    }

    @VisibleForTesting
    public ManagementContextProvider(ManagementContext mgmt) {
        this.mgmt = mgmt;
    }

    @Override
    public ManagementContext getContext(Class<?> type) {
        if (type == ManagementContext.class) {
            if (mgmt != null) {
                return mgmt;
            } else {
                return (ManagementContext) context.getAttribute(BrooklynServiceAttributes.BROOKLYN_MANAGEMENT_CONTEXT);
            }
        } else {
            return null;
        }
    }

}
