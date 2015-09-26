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
package org.fcrepo.sword.protocol;

/**
 * Defines SWORD protocol related namespaces and constants
 *
 * @author claussni
 */
public class SWORDProtocol {
    public static final String SWORD_NAMESPACE       = "http://purl.org/net/sword/";
    public static final String SWORD_TERMS_NAMESPACE = "http://purl.org/net/sword/terms/";
    public static final String SWORD_VERSION         = "2.0";

    private SWORDProtocol() {
        // Utility classes should not have a public or default constructor.
    }
}
