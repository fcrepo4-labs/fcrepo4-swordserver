/*
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
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.IOException;

import static org.fcrepo.sword.integration.Assert.assertContentType;
import static org.fcrepo.sword.integration.Assert.assertStatusCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author claussni
 */
public class ServiceDocumentIT extends BaseServiceProviderIT {

    @Test
    public void returnsAtomServiceDocumentMediaType() throws IOException {
        final HttpResponse response = requestServiceDocument();
        assertStatusCode(200, response);
        assertContentType("application/atomsvc+xml", response);
    }

    @Test
    public void specifiesSwordVersion2() throws IOException {
        final HttpResponse response = requestServiceDocument();
        final Service service = serviceDocumentFromStream(response.getEntity().getContent());
        assertEquals("2.0", service.getSimpleExtension(
                "http://purl.org/net/sword/terms/",
                "version",
                "sword"));
    }

    @Test
    public void specifiesMaxUploadSizeAsInteger() throws IOException {
        final HttpResponse response = requestServiceDocument();
        final Service service = serviceDocumentFromStream(response.getEntity().getContent());
        try {
            final int maxUploadSize = Integer.parseUnsignedInt(service.getSimpleExtension(
                    "http://purl.org/net/sword/terms/",
                    "maxUploadSize",
                    "sword"));
            assertEquals("Expected default value for sword:maxUploadSize", Integer.MAX_VALUE, maxUploadSize);
        } catch (NumberFormatException e) {
            fail("sword:maxUploadSize is not an Integer");
        }
    }

    @Test
    public void canCreateNewWorkspace() throws IOException {
        final HttpResponse response = createWorkspaceNode("Test Workspace");
        assertEquals("Expect 201 Created response", HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
    }

    @Test
    public void definedWorkspaceGetsListetInServiceDocument() throws IOException {
        final String title = "Test Workspace";
        createWorkspaceNode(title);
        final HttpResponse response = requestServiceDocument();
        final Service service = serviceDocumentFromStream(response.getEntity().getContent());
        assertNotNull("Expect workspace to be defined.", service.getWorkspace(title));
        assertEquals(title, service.getWorkspace(title).getTitle());
    }

}

