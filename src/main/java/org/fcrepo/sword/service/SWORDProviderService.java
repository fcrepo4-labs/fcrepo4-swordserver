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

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Service;
import org.fcrepo.http.commons.session.SessionFactory;
import org.modeshape.jcr.api.NamespaceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * @author claussni
 */
@Component
public class SWORDProviderService {

    public static final String NS_SWORD   = "http://purl.org/net/sword/";
    public static final String RDF_PREFIX = "sword";
    private final Abdera abdera;

    @Autowired
    private SessionFactory sessionFactory;

    /**
     *
     */
    public SWORDProviderService() {
        abdera = new Abdera();
    }

    /**
     * Service intitialization
     *
     * @throws RepositoryException the repository exception
     */
    @PostConstruct
    public void init() throws RepositoryException {
        /* check if set root node exists */
        final Session session = sessionFactory.getInternalSession();

        final NamespaceRegistry namespaceRegistry =
                (org.modeshape.jcr.api.NamespaceRegistry) session.getWorkspace().getNamespaceRegistry();

        // Register the sword namespace if it's not found
        if (!namespaceRegistry.isRegisteredPrefix(RDF_PREFIX)) {
            namespaceRegistry.registerNamespace(RDF_PREFIX, NS_SWORD);
        }
    }

    /**
     *
     * @return
     */
    public Service serviceDocument() {
        final Service service = abdera.newService();
        service.addSimpleExtension(
                "http://purl.org/net/sword/terms/",
                "version",
                "sword",
                "2.0");
        return service;
    }


}
