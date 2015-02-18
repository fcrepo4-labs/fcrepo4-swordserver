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
package org.fcrepo.sword;

import io.dropwizard.Application;
import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.fcrepo.sword.core.CollectionDepositManagerImpl;
import org.fcrepo.sword.core.CollectionListManagerImpl;
import org.fcrepo.sword.core.ContainerManagerImpl;
import org.fcrepo.sword.core.MediaResourceManagerImpl;
import org.fcrepo.sword.core.ServiceDocumentManagerImpl;
import org.fcrepo.sword.core.StatementManagerImpl;
import org.fcrepo.sword.health.DummyHealthCheck;
import org.swordapp.server.SwordConfigurationDefault;
import org.swordapp.server.servlets.CollectionServletDefault;
import org.swordapp.server.servlets.ContainerServletDefault;
import org.swordapp.server.servlets.MediaResourceServletDefault;
import org.swordapp.server.servlets.ServiceDocumentServletDefault;
import org.swordapp.server.servlets.StatementServletDefault;

/**
 * @author claussni
 */
public class SwordApplication extends Application<SwordApplicationConfiguration> {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        new SwordApplication().run(args);
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return "sword-application";
    }

    /**
     * @param bootstrap
     */
    @Override
    public void initialize(final Bootstrap<SwordApplicationConfiguration> bootstrap) {
    }

    /**
     * @param configuration
     * @param environment
     * @throws Exception
     */
    @Override
    public void run(
            final SwordApplicationConfiguration configuration,
            final Environment environment) throws Exception {
        environment.jersey().disable();
        environment.healthChecks().register("dummy", new DummyHealthCheck());

        final MutableServletContextHandler applicationContext = environment.getApplicationContext();
        applicationContext.setInitParameter("config-impl", SwordConfigurationDefault.class.getName());
        applicationContext.setInitParameter("service-document-impl", ServiceDocumentManagerImpl.class.getName());
        applicationContext.setInitParameter("collection-list-impl", CollectionListManagerImpl.class.getName());
        applicationContext.setInitParameter("collection-deposit-impl", CollectionDepositManagerImpl.class.getName());
        applicationContext.setInitParameter("media-resource-impl", MediaResourceManagerImpl.class.getName());
        applicationContext.setInitParameter("container-impl", ContainerManagerImpl.class.getName());
        applicationContext.setInitParameter("statement-impl", StatementManagerImpl.class.getName());
        applicationContext.setInitParameter("authentication-method", "Basic");

        applicationContext.addServlet(ServiceDocumentServletDefault.class.getName(), "/servicedocument");
        applicationContext.addServlet(CollectionServletDefault.class.getName(), "/collection");
        applicationContext.addServlet(MediaResourceServletDefault.class.getName(), "/edit-media");
        applicationContext.addServlet(ContainerServletDefault.class.getName(), "/edit");
        applicationContext.addServlet(StatementServletDefault.class.getName(), "/statement");
    }

}
