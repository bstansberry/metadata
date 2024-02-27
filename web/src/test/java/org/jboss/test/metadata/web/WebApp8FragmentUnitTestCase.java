/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.test.metadata.web;

import org.jboss.metadata.merge.javaee.spec.JavaEEVersion;
import org.jboss.metadata.web.spec.WebFragmentMetaData;

/**
 * Test all entries of javaee 7 web-fragment.
 *
 * @author Eduardo Martins
 *
 */
public class WebApp8FragmentUnitTestCase extends WebApp6FragmentUnitTestCase {

    public void testEverything() throws Exception {
        WebFragmentMetaData webApp = unmarshal();
        assertEverything(webApp, Mode.SPEC, JavaEEVersion.V8);
    }

}
