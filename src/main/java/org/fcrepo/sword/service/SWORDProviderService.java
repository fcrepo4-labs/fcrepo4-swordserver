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
package org.fcrepo.sword.service;

import com.hp.hpl.jena.rdf.model.Property;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Service;
import org.fcrepo.http.commons.session.SessionFactory;
import org.fcrepo.kernel.models.Container;
import org.fcrepo.kernel.services.ContainerService;
import org.fcrepo.kernel.services.NodeService;
import org.modeshape.jcr.api.NamespaceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Map;

import static com.hp.hpl.jena.rdf.model.ResourceFactory.createProperty;
import static java.util.Collections.emptyMap;
import static org.fcrepo.kernel.impl.rdf.converters.PropertyConverter.getPropertyNameFromPredicate;

/**
 * @author claussni
 */
@Component
public class SWORDProviderService {

    public static final  String  NS_SWORD                 = "http://purl.org/net/sword/";
    public static final  String  NS_SWORD_TERMS           = "http://purl.org/net/sword/terms/";
    public static final  String  RDF_PREFIX               = "sword";
    public static final  String  SWORD_ROOT_PATH          = "/sword";
    public static final  String  SWORD_COLLECTIONS_PATH   = SWORD_ROOT_PATH + "/collections";
    public static final  String  SWORD_WORKSPACES_PATH    = SWORD_ROOT_PATH + "/workspaces";
    public static final  String  SWORD_VERSION            = "2.0";
    public static final  Boolean SWORD_SUPPORTS_VERBOSE   = true;
    public static final  Boolean SWORD_SUPPORTS_NOOP      = true;
    public static final  Integer SWORD_MAX_UPLOAD_SIZE_KB = Integer.MAX_VALUE;
    public static final  String  NS_FEDORA_DEFINITIONS    = "http://fedora.info/definitions/v4/config#";
    private static final Logger  log                      = LoggerFactory.getLogger(SWORDProviderService.class);
    private final        Abdera  abdera                   = new Abdera();

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ContainerService containerService;

    /**
     * Service intitialization
     *
     * @throws RuntimeException if initialization failes.
     */
    @PostConstruct
    private void init() {
        final Session session = sessionFactory.getInternalSession();
        registerNamespace(session);
        final Container root = initializeContainer(session, SWORD_ROOT_PATH, "SWORD root");
        ensureDefaultProperties(session, root);
        initializeContainer(session, SWORD_WORKSPACES_PATH, "SWORD workspaces");
        initializeContainer(session, SWORD_COLLECTIONS_PATH, "SWORD collections");
    }

    private void ensureDefaultProperties(final Session session, final Container root) {
        try {
            ensureProperty(session, root, NS_FEDORA_DEFINITIONS + "supportsVerbose", SWORD_SUPPORTS_VERBOSE);
            ensureProperty(session, root, NS_FEDORA_DEFINITIONS + "supportsNoop", SWORD_SUPPORTS_NOOP);
            ensureProperty(session, root, NS_FEDORA_DEFINITIONS + "maxUploadSizeInKb", SWORD_MAX_UPLOAD_SIZE_KB);
            session.save();
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to initialize default properties", e);
        }
    }

    private void ensureProperty(
            final Session session,
            final Container root,
            final String name,
            final Object defaultValue) {
        try {
            final String propertyName = getPropertyName(session, createProperty(name));
            if (!root.getNode().hasProperty(propertyName)) {
                root.getNode().setProperty(propertyName, String.valueOf(defaultValue));
            }
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to initialize property " + name, e);
        }
    }

    /**
     * Get a property name for an RDF predicate
     *
     * @param session
     * @param predicate
     * @return property name from the given predicate
     * @throws RepositoryException
     */
    private String getPropertyName(final Session session, final Property predicate)
            throws RepositoryException {

        final NamespaceRegistry namespaceRegistry =
                (org.modeshape.jcr.api.NamespaceRegistry) session.getWorkspace().getNamespaceRegistry();
        final Map<String, String> namespaceMapping = emptyMap();
        return getPropertyNameFromPredicate(namespaceRegistry, predicate, namespaceMapping);
    }

    private Container initializeContainer(final Session session, final String path, final String label) {
        try {
            if (!nodeService.exists(session, path)) {
                log.info("Initializing " + label + " container {}", path);
                final Container c = containerService.findOrCreate(session, path);
                session.save();
                return c;
            }
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to initialize " + label + " container", e);
        }
        return null;
    }

    private void registerNamespace(final Session session) {
        try {
            final NamespaceRegistry namespaceRegistry;
            namespaceRegistry = (NamespaceRegistry) session.getWorkspace().getNamespaceRegistry();

            // Register the sword namespace if it's not found
            if (!namespaceRegistry.isRegisteredPrefix(RDF_PREFIX)) {
                namespaceRegistry.registerNamespace(RDF_PREFIX, NS_SWORD);
            }
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to register SWORD namespace", e);
        }
    }

    /**
     * @return
     */
    public Service serviceDocument() {
        final Service service = abdera.newService();
        service.addSimpleExtension(NS_SWORD_TERMS, "version", "sword", SWORD_VERSION);
        return service;
    }


}
