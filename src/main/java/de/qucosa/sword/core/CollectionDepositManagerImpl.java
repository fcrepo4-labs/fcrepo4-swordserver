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

package de.qucosa.sword.core;

import org.swordapp.server.*;

public class CollectionDepositManagerImpl implements CollectionDepositManager {
    @Override
    public DepositReceipt createNew(
            String collectionURI,
            Deposit deposit,
            AuthCredentials auth,
            SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        throw new UnsupportedOperationException();
    }
}
