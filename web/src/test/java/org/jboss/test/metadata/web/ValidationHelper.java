/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.test.metadata.web;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmanuel Hugonnet (c) 2018 Red Hat, inc.
 */
public class ValidationHelper {
    private static InputSource inputSource(final Class loader, final String systemId, final String resource) {
        final InputStream in = loader.getResourceAsStream(resource);
        if (in == null) { throw new IllegalArgumentException("Can't find resource " + resource); }
        final InputSource inputSource = new InputSource(in);
        inputSource.setSystemId(systemId);
        return inputSource;
    }

    public static Document parse(final InputSource is, final Class loader) throws ParserConfigurationException, IOException, SAXException {
        // parse an XML document into a DOM tree
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(
                "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                "http://www.w3.org/2001/XMLSchema");
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        DocumentBuilder parser = factory.newDocumentBuilder();
        final Map<String, String> system = new HashMap<>();
        system.put("http://www.jboss.org/schema/jbossas/javaee_7.xsd", "/schema/javaee_7.xsd");
        system.put("https://www.jboss.org/schema/jbossas/javaee_8.xsd", "/schema/javaee_8.xsd");
        system.put("http://www.jboss.org/schema/jbossas/javaee_web_services_client_1_4.xsd", "/schema/javaee_web_services_client_1_4.xsd");
        system.put("http://www.jboss.org/schema/jbossas/jsp_2_3.xsd", "/schema/jsp_2_3.xsd");
        system.put("http://www.w3.org/2001/xml.xsd", "/schema/xml.xsd");
        system.put("http://www.jboss.org/schema/jbossas/web-app_3_1.xsd", "/schema/web-app_3_1.xsd");
        system.put("http://www.jboss.org/schema/jbossas/web-app_4_0.xsd", "/schema/web-app_4_0.xsd");
        system.put("http://www.jboss.org/schema/jbossas/jboss-common_7_1.xsd", "/schema/jboss-common_7_1.xsd");
        system.put("http://www.jboss.org/schema/jbossas/jboss-common_8_0.xsd", "/schema/jboss-common_8_0.xsd");
        system.put("https://www.jboss.org/schema/jbossas/jboss-common_8_1.xsd", "/schema/jboss-common_8_1.xsd");
        system.put("http://www.jboss.org/schema/jbossas/web-common_3_1.xsd", "/schema/web-common_3_1.xsd");
        system.put("http://www.jboss.org/schema/jbossas/web-common_4_0.xsd", "/schema/web-common_4_0.xsd");
        system.put("http://www.jboss.org/j2ee/schema/jboss-web_13_0.xsd", "/schema/jboss-web_13_0.xsd");
        system.put("http://www.jboss.org/j2ee/schema/jboss-web_12_1.xsd", "/schema/jboss-web_12_1.xsd");
        system.put("https://www.jboss.org/j2ee/schema/jboss-web_14_1.xsd", "/schema/jboss-web_14_1.xsd");

        parser.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
//            System.out.println("resolve " + publicId + ", " + systemId);
                final String resource = system.get(systemId);
                if (resource == null) { throw new SAXException("Can't resolve systemId " + systemId); }
                return inputSource(loader, systemId, resource);
            }
        });
        parser.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                throw exception;
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                //System.err.println(exception.getPublicId() + " " + exception.getSystemId() + " " + exception.getLineNumber() + " " + exception.getColumnNumber() + " " + exception.getMessage());
                throw exception;
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }
        });
        return parser.parse(is);
    }
}
