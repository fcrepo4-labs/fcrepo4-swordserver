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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.fcrepo.sword.service.SWORDProviderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.fcrepo.sword.integration.Assert.assertStatusCode;

/**
 * @author claussni
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-test/test-container.xml")
public abstract class BaseProviderServiceIT {

    protected static final int SERVER_PORT = Integer.parseInt(System
            .getProperty("fcrepo.dynamic.test.port", "8080"));

    protected static final String HOSTNAME = "localhost";

    protected static final String SERVICE_NAME = "fcr:sword";

    protected static final String serverAddress =
            String.format("http://%s:%s/%s", HOSTNAME, SERVER_PORT, SERVICE_NAME);

    protected HttpClient httpClient;

    protected Service serviceDocumentFromStream(final InputStream content) {
        final Abdera abdera = new Abdera();
        final Document<Service> serviceDocument = abdera.getParser().parse(content);
        return serviceDocument.getRoot();
    }

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

    protected HttpResponse requestServiceDocument() throws IOException {
        final HttpGet get = new HttpGet(serverAddress);
        get.setHeader("Content-Type", "application/svc+xml");
        return httpClient.execute(get);
    }

    protected HttpResponse createWorkspaceNode(final String title) throws IOException {
        final HttpPost post = new HttpPost(String.format("http://%s:%s/%s/",
                HOSTNAME,
                SERVER_PORT,
                SWORDProviderService.SWORD_WORKSPACES_PATH));
        post.setHeader("Content-Type", "text/turtle");
        post.setEntity(new StringEntity(
                String.format("PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
                        "<> dc:title \"%s\"", title)));
        return httpClient.execute(post);
    }
}
