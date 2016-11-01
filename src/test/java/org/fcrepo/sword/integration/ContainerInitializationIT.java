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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.fcrepo.sword.provider.SWORDServiceProvider;
import org.junit.Test;

import java.io.IOException;

import static org.fcrepo.sword.integration.Assert.assertStatusCode;

/**
 * @author claussni
 */
public class ContainerInitializationIT extends BaseServiceProviderIT {

    @Test
    public void initializesRootContainer() throws IOException {
        final HttpGet get = new HttpGet(String.format("http://%s:%s/%s",
                HOSTNAME,
                SERVER_PORT,
                SWORDServiceProvider.SWORD_ROOT_PATH));
        final HttpResponse response = httpClient.execute(get);
        assertStatusCode(200, response);
    }

    @Test
    public void initializesWorkspacesContainer() throws IOException {
        final HttpGet get = new HttpGet(String.format("http://%s:%s/%s",
                HOSTNAME,
                SERVER_PORT,
                SWORDServiceProvider.SWORD_WORKSPACES_PATH));
        final HttpResponse response = httpClient.execute(get);
        assertStatusCode(200, response);
    }

    @Test
    public void initializesCollectionsContainer() throws IOException {
        final HttpGet get = new HttpGet(String.format("http://%s:%s/%s",
                HOSTNAME,
                SERVER_PORT,
                SWORDServiceProvider.SWORD_COLLECTIONS_PATH));
        final HttpResponse response = httpClient.execute(get);
        assertStatusCode(200, response);
    }

}
