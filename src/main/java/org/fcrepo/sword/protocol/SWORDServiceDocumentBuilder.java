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
package org.fcrepo.sword.protocol;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Service;
import org.fcrepo.kernel.api.models.Container;
import org.fcrepo.kernel.api.models.FedoraResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Value;

import static org.fcrepo.sword.protocol.SWORDProtocol.SWORD_TERMS_NAMESPACE;
import static org.fcrepo.sword.protocol.SWORDProtocol.SWORD_VERSION;

/**
 * Builder for a SWORD service document using Abdera ATOM library
 *
 * @author claussni
 */
public class SWORDServiceDocumentBuilder {

    private Abdera    abdera;
    private Integer   maxUploadSize;
    private Container workspaces;
    private Logger    log;

    /**
     * Create a new builder
     *
     * @param abdera Abdera implementation to use
     * @param log    Logger implementation to use. If none is given, a default logger for this class is used.
     */
    public SWORDServiceDocumentBuilder(final Abdera abdera, final Logger log) {
        this.abdera = abdera;
        this.log = (log == null) ? LoggerFactory.getLogger(SWORDServiceDocumentBuilder.class) : log;
    }

    /**
     * Set the maxUploadSize parameter
     *
     * @param swordMaxUploadSizeKb Maximum size of uploads in kilobytes
     * @return The builder object
     */
    public SWORDServiceDocumentBuilder maxUploadSize(final Integer swordMaxUploadSizeKb) {
        this.maxUploadSize = swordMaxUploadSizeKb;
        return this;
    }

    /**
     * Set the Fedora container resource containing the SWORD workspace nodes
     *
     * @param workspaces Container with workspace nodes
     * @return The builder object
     */
    public SWORDServiceDocumentBuilder workspacesContainer(final Container workspaces) {
        this.workspaces = workspaces;
        return this;
    }

    /**
     * Build and return the service document containing the configured parameters and detected workspaces
     *
     * @return A SWORD service document
     */
    public Service serviceDocument() {
        final Service service = abdera.newService();
        service.addSimpleExtension(SWORD_TERMS_NAMESPACE, "version", "sword", SWORD_VERSION);
        service.addSimpleExtension(SWORD_TERMS_NAMESPACE, "maxUploadSize", "sword", String.valueOf(maxUploadSize));
        workspaces.getChildren().forEach(fedoraResource -> addWorkspace(service, fedoraResource));
        return service;
    }

    private void addWorkspace(final Service service, final FedoraResource fedoraResource) {
        try {
            final Value[] dcTitles = fedoraResource.getNode().getProperty("dc:title").getValues();
            if (dcTitles.length > 0) {
                try {
                    final String title = dcTitles[0].getString();
                    if (!title.isEmpty()) {
                        service.addWorkspace(title);
                    }
                } catch (IndexOutOfBoundsException | NullPointerException e) {
                    log.warn("Found workspace container without dc:title property: {}", fedoraResource.getPath());
                }
            }
        } catch (RepositoryException e) {
            log.warn("Found workspace container with invalid dc:title property: {}", fedoraResource.getPath());
        }
    }

}
