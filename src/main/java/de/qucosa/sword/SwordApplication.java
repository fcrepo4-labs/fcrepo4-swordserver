/*
 * Copyright 2014 Saxon State and University Library Dresden (SLUB)
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

package de.qucosa.sword;

import de.qucosa.sword.core.*;
import de.qucosa.sword.health.DummyHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.swordapp.server.SwordConfigurationDefault;
import org.swordapp.server.servlets.*;

public class SwordApplication extends Application<SwordApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new SwordApplication().run(args);
    }

    @Override
    public String getName() {
        return "sword-application";
    }

    @Override
    public void initialize(Bootstrap<SwordApplicationConfiguration> bootstrap) {
    }

    @Override
    public void run(SwordApplicationConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().disable();
        environment.healthChecks().register("dummy", new DummyHealthCheck());

        environment.getApplicationContext().setInitParameter("config-impl", SwordConfigurationDefault.class.getName());
        environment.getApplicationContext().setInitParameter("service-document-impl", ServiceDocumentManagerImpl.class.getName());
        environment.getApplicationContext().setInitParameter("collection-list-impl", CollectionListManagerImpl.class.getName());
        environment.getApplicationContext().setInitParameter("collection-deposit-impl", CollectionDepositManagerImpl.class.getName());
        environment.getApplicationContext().setInitParameter("media-resource-impl", MediaResourceManagerImpl.class.getName());
        environment.getApplicationContext().setInitParameter("container-impl", ContainerManagerImpl.class.getName());
        environment.getApplicationContext().setInitParameter("statement-impl", StatementManagerImpl.class.getName());
        environment.getApplicationContext().setInitParameter("authentication-method", "Basic");

        environment.getApplicationContext().addServlet(ServiceDocumentServletDefault.class.getName(), "/servicedocument");
        environment.getApplicationContext().addServlet(CollectionServletDefault.class.getName(), "/collection");
        environment.getApplicationContext().addServlet(MediaResourceServletDefault.class.getName(), "/edit-media");
        environment.getApplicationContext().addServlet(ContainerServletDefault.class.getName(), "/edit");
        environment.getApplicationContext().addServlet(StatementServletDefault.class.getName(), "/statement");
    }

}
