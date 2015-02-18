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
import org.swordapp.server.ContainerManager;
import org.swordapp.server.Deposit;
import org.swordapp.server.DepositReceipt;
import org.swordapp.server.SwordAuthException;
import org.swordapp.server.SwordConfiguration;
import org.swordapp.server.SwordError;
import org.swordapp.server.SwordServerException;

import java.util.Map;

/**
 * @author claussni
 */
public class ContainerManagerImpl implements ContainerManager {
    /**
     * @param editIRI
     * @param accept
     * @param auth
     * @param config
     * @return
     * @throws SwordServerException
     * @throws SwordError
     * @throws SwordAuthException
     */
    @Override
    public DepositReceipt getEntry(
            final String editIRI,
            final Map<String, String> accept,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordServerException, SwordError, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param editIRI
     * @param deposit
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public DepositReceipt replaceMetadata(
            final String editIRI,
            final Deposit deposit,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param editIRI
     * @param deposit
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public DepositReceipt replaceMetadataAndMediaResource(
            final String editIRI,
            final Deposit deposit,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param editIRI
     * @param deposit
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public DepositReceipt addMetadataAndResources(
            final String editIRI,
            final Deposit deposit,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param editIRI
     * @param deposit
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public DepositReceipt addMetadata(
            final String editIRI,
            final Deposit deposit,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param editIRI
     * @param deposit
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public DepositReceipt addResources(
            final String editIRI,
            final Deposit deposit,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param editIRI
     * @param auth
     * @param config
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public void deleteContainer(
            final String editIRI,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param editIRI
     * @param deposit
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public DepositReceipt useHeaders(
            final String editIRI,
            final Deposit deposit,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param editIRI
     * @param accept
     * @param auth
     * @param config
     * @return
     * @throws SwordError
     * @throws SwordServerException
     * @throws SwordAuthException
     */
    @Override
    public boolean isStatementRequest(
            final String editIRI,
            final Map<String, String> accept,
            final AuthCredentials auth,
            final SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }
}
