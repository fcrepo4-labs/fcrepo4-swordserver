/**
 * Copyright 2015 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fcrepo.sword.http;

import org.apache.abdera.model.Service;
import org.fcrepo.sword.service.SWORDProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author claussni
 * @since 18.02.15.
 */
@Scope("request")
@Path("/fcr:sword")
public class SWORDServiceResource {

    @Autowired
    private SWORDProviderService providerService;

    /**
     * @return Returns a SWORD service document
     */
    @GET
    @Produces("application/atomsvc+xml")
    public Service serviceDocument() {
        return providerService.serviceDocument();
    }

}
