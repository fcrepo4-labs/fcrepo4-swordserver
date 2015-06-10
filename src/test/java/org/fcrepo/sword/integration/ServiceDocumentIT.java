/**
 * Copyright 2015 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fcrepo.sword.integration;

import org.apache.abdera.model.Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import java.io.IOException;

import static org.fcrepo.sword.integration.Assert.assertContentType;
import static org.fcrepo.sword.integration.Assert.assertStatusCode;
import static org.junit.Assert.assertEquals;

/**
 * @author claussni
 */
public class ServiceDocumentIT extends BaseProviderServiceIT {

    @Test
    public void returnsAtomServiceDocumentMediaType() throws IOException {
        final HttpGet get = new HttpGet(serverAddress);
        get.setHeader("Content-Type", "application/svc+xml");
        final HttpResponse response = httpClient.execute(get);
        assertStatusCode(200, response);
        assertContentType("application/atomsvc+xml", response);
    }

    @Test
    public void hasSwordVersion2() throws IOException {
        final HttpGet get = new HttpGet(serverAddress);
        get.setHeader("Content-Type", "application/svc+xml");
        final HttpResponse response = httpClient.execute(get);
        final Service      service  = serviceDocumentFromStream(response.getEntity().getContent());
        assertEquals("2.0", service.getSimpleExtension(
                "http://purl.org/net/sword/terms/",
                "version",
                "sword"));
    }

}
