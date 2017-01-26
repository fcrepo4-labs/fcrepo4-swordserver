/*
 * Licensed to DuraSpace under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 *
 * DuraSpace licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
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
import org.fcrepo.sword.provider.SWORDServiceProvider;
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
public abstract class BaseServiceProviderIT {

    static final int SERVER_PORT = sytemPropertyOrDefault("fcrepo.dynamic.test.port", "8080");
    static final String HOSTNAME = "localhost";
    private static final String SERVICE_NAME = "fcr:sword";
    HttpClient httpClient;

    private static int sytemPropertyOrDefault(final String key, final String def) {
        String val = System.getProperty(key, "");
        return Integer.parseInt((val.isEmpty()) ? def : val);
    }

    Service serviceDocumentFromStream(final InputStream content) {
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
        final HttpGet get = new HttpGet(String.format("http://%s:%s/%s", HOSTNAME, SERVER_PORT, SERVICE_NAME));
        get.setHeader("Content-Type", "application/svc+xml");
        return httpClient.execute(get);
    }

    protected HttpResponse createWorkspaceNode(final String title) throws IOException {
        final HttpPost post = new HttpPost(String.format("http://%s:%s/%s/",
                HOSTNAME,
                SERVER_PORT,
                SWORDServiceProvider.SWORD_WORKSPACES_PATH));
        post.setHeader("Content-Type", "text/turtle");
        post.setEntity(new StringEntity(
                String.format("PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
                        "<> dc:title \"%s\"", title)));
        return httpClient.execute(post);
    }
}
