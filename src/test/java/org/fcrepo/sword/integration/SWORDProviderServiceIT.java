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

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author claussni
 * @since 19.02.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-test/test-container.xml")
public class SWORDProviderServiceIT {

    protected static final int SERVER_PORT = Integer.parseInt(System
            .getProperty("fcrepo.dynamic.test.port", "8080"));

    protected static final String HOSTNAME = "localhost";

    private static final String SERVICE_NAME = "fcr:sword";

    protected static final String serverAddress =
            String.format("http://%s:%s/%s", HOSTNAME, SERVER_PORT, SERVICE_NAME);

    protected HttpClient httpClient;

    @Before
    public void setupHttpClient() {
        httpClient = HttpClientBuilder.create().build();
    }

    @Test
    public void fedoraRepositoryIsResponding() throws IOException {
        final HttpGet get = new HttpGet(String.format("http://%s:%s/", HOSTNAME, SERVER_PORT));
        final HttpResponse response = httpClient.execute(get);
        assertStatusCode(200, response);
    }

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
        final Service service = serviceDocumentFromStream(response.getEntity().getContent());
        assertEquals("2.0", service.getSimpleExtension(
                "http://purl.org/net/sword/terms/",
                "version",
                "sword"));
    }

    private Service serviceDocumentFromStream(final InputStream content) {
        final Abdera abdera = new Abdera();
        final Document<Service> serviceDocument = abdera.getParser().parse(content);
        return serviceDocument.getRoot();
    }

    private void assertStatusCode(final int statusCode, final HttpResponse httpResponse) {
        assertEquals("Expected different status code", statusCode, httpResponse.getStatusLine().getStatusCode());
    }

    private void assertContentType(final String contentType, final HttpResponse httpResponse) {
        final String contentTypeHeader = httpResponse.getFirstHeader("Content-Type").getValue();
        assertTrue("Response has wrong content type", contentTypeHeader.startsWith(contentType));
    }
}
