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
package org.fcrepo.sword.provider;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Service;
import org.fcrepo.http.commons.session.SessionFactory;
import org.fcrepo.kernel.api.RdfLexicon;
import org.fcrepo.kernel.api.models.Container;
import org.fcrepo.kernel.api.services.ContainerService;
import org.fcrepo.kernel.api.services.NodeService;
import org.fcrepo.kernel.modeshape.FedoraResourceImpl;
import org.fcrepo.kernel.modeshape.utils.NamespaceTools;
import org.fcrepo.sword.protocol.SWORDServiceDocumentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jcr.NamespaceRegistry;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Map;

import static com.hp.hpl.jena.rdf.model.ResourceFactory.createProperty;
import static java.util.Collections.emptyMap;
import static org.fcrepo.kernel.modeshape.rdf.converters.PropertyConverter.getPropertyNameFromPredicate;
import static org.fcrepo.sword.protocol.SWORDProtocol.SWORD_NAMESPACE;

/**
 * Implements all protocol related methods to be executed via {@link org.fcrepo.sword.http.SWORDWebResource}
 *
 * @author claussni
 */
@Component
public class SWORDServiceProvider {

    public static final String SWORD_ROOT_PATH        = "/sword";
    public static final String SWORD_COLLECTIONS_PATH = SWORD_ROOT_PATH + "/collections";
    public static final String SWORD_WORKSPACES_PATH  = SWORD_ROOT_PATH + "/workspaces";

    private static final String  RDF_PREFIX               = "sword";
    private static final String  SWORD_ROOT_LABEL         = "SWORD root";
    private static final Integer SWORD_MAX_UPLOAD_SIZE_KB = Integer.MAX_VALUE;

    private static final Logger log    = LoggerFactory.getLogger(SWORDServiceProvider.class);
    private final        Abdera abdera = new Abdera();

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ContainerService containerService;

    private Container workspaces;

    /**
     * Build and return a SWORD service document
     *
     * @return SWORD service document
     */
    public Service serviceDocument() {
        final SWORDServiceDocumentBuilder sb = new SWORDServiceDocumentBuilder(abdera, log)
                .maxUploadSize(SWORD_MAX_UPLOAD_SIZE_KB)
                .workspacesContainer(workspaces);
        return sb.serviceDocument();
    }

    @PostConstruct
    private void init() {
        final Session session = sessionFactory.getInternalSession();
        final NamespaceRegistry namespaceRegistry = NamespaceTools.getNamespaceRegistry(session);

        ensureSwordNamespaceRegistration(namespaceRegistry);

        final Container root = getContainer(session, SWORD_ROOT_PATH, SWORD_ROOT_LABEL);
        ensureProperty(namespaceRegistry, root,
                RdfLexicon.FEDORA_CONFIG_NAMESPACE + "maxUploadSizeInKb", SWORD_MAX_UPLOAD_SIZE_KB);

        workspaces = getContainer(session, SWORD_WORKSPACES_PATH, "SWORD workspaces");
        getContainer(session, SWORD_COLLECTIONS_PATH, "SWORD collections");
    }

    private void ensureSwordNamespaceRegistration(final NamespaceRegistry namespaceRegistry) {
        try {
            namespaceRegistry.registerNamespace(RDF_PREFIX, SWORD_NAMESPACE);
        } catch (RepositoryException e) {
            throw new RuntimeException(
                    String.format("Failed to register namespace %s:%s", RDF_PREFIX, SWORD_NAMESPACE), e);
        }
    }

    private Container getContainer(final Session session, final String path, final String label) {
        try {
            if (!nodeService.exists(session, path)) {
                log.info("Initializing container `{}` at `{}`", label, path);
                final Container c = containerService.findOrCreate(session, path);
                session.save();
                return c;
            }
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to initialize container " + label, e);
        }
        return null;
    }

    private void ensureProperty(
            final NamespaceRegistry namespaceRegistry,
            final Container container,
            final String name,
            final Object defaultValue) {
        try {
            final Map<String, String> namespaceMapping = emptyMap();
            final String propertyName = getPropertyNameFromPredicate(
                    (org.modeshape.jcr.api.NamespaceRegistry) namespaceRegistry,
                    createProperty(name),
                    namespaceMapping);

            // FIXME Setting container property bypassing the API
            final FedoraResourceImpl containerNode = (FedoraResourceImpl) container;

            if (!containerNode.hasProperty(propertyName)) {
                containerNode.getNode().setProperty(propertyName, String.valueOf(defaultValue));
            }
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to initialize property " + name, e);
        }
    }

}
