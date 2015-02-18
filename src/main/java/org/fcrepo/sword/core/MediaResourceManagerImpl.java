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
package org.fcrepo.sword.core;

import org.swordapp.server.AuthCredentials;
import org.swordapp.server.Deposit;
import org.swordapp.server.DepositReceipt;
import org.swordapp.server.MediaResource;
import org.swordapp.server.MediaResourceManager;
import org.swordapp.server.SwordAuthException;
import org.swordapp.server.SwordConfiguration;
import org.swordapp.server.SwordError;
import org.swordapp.server.SwordServerException;

import java.util.Map;

/**
 * @author claussni
 */
public class MediaResourceManagerImpl implements MediaResourceManager {
    /**
     * @param uri
     * @param accept
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public MediaResource getMediaResourceRepresentation(
            final String uri,
            final Map<String, String> accept,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param uri
     * @param deposit
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public DepositReceipt replaceMediaResource(
            final String uri,
            final Deposit deposit,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param uri
     * @param auth
     * @param config
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public void deleteMediaResource(
            final String uri,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param uri
     * @param deposit
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public DepositReceipt addResource(
            final String uri,
            final Deposit deposit,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }
}
