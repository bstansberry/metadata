/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.parser.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.metadata.javaee.spec.DescriptionGroupMetaData;
import org.jboss.metadata.parser.ee.DescriptionGroupMetaDataParser;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.property.PropertyReplacer;
import org.jboss.metadata.web.spec.JspPropertyGroupMetaData;

/**
 * @author Remy Maucherat
 */
public class JspPropertyGroupMetaDataParser extends MetaDataElementParser {

    public static JspPropertyGroupMetaData parse(XMLStreamReader reader, PropertyReplacer propertyReplacer) throws XMLStreamException {
        JspPropertyGroupMetaData jspPropertyGroup = new JspPropertyGroupMetaData();

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case ID: {
                    jspPropertyGroup.setId(value);
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        DescriptionGroupMetaData descriptionGroup = new DescriptionGroupMetaData();
        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            if (DescriptionGroupMetaDataParser.parse(reader, descriptionGroup)) {
                if (jspPropertyGroup.getDescriptionGroup() == null) {
                    jspPropertyGroup.setDescriptionGroup(descriptionGroup);
                }
                continue;
            }
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case URL_PATTERN:
                    List<String> urlPatterns = jspPropertyGroup.getUrlPatterns();
                    if (urlPatterns == null) {
                        urlPatterns = new ArrayList<String>();
                        jspPropertyGroup.setUrlPatterns(urlPatterns);
                    }
                    urlPatterns.add(getElementText(reader, propertyReplacer));
                    break;
                case EL_IGNORED:
                    jspPropertyGroup.setElIgnored(getElementText(reader, propertyReplacer));
                    break;
                case ERROR_ON_EL_NOT_FOUND:
                    jspPropertyGroup.setErrorOnElNotFound(getElementText(reader, propertyReplacer));
                    break;
                case PAGE_ENCODING:
                    jspPropertyGroup.setPageEncoding(getElementText(reader, propertyReplacer));
                    break;
                case SCRIPTING_INVALID:
                    jspPropertyGroup.setScriptingInvalid(getElementText(reader, propertyReplacer));
                    break;
                case IS_XML:
                    jspPropertyGroup.setIsXml(getElementText(reader, propertyReplacer));
                    break;
                case INCLUDE_PRELUDE:
                    List<String> includePreludes = jspPropertyGroup.getIncludePreludes();
                    if (includePreludes == null) {
                        includePreludes = new ArrayList<String>();
                        jspPropertyGroup.setIncludePreludes(includePreludes);
                    }
                    includePreludes.add(getElementText(reader, propertyReplacer));
                    break;
                case INCLUDE_CODA:
                    List<String> includeCodas = jspPropertyGroup.getIncludeCodas();
                    if (includeCodas == null) {
                        includeCodas = new ArrayList<String>();
                        jspPropertyGroup.setIncludeCodas(includeCodas);
                    }
                    includeCodas.add(getElementText(reader, propertyReplacer));
                    break;
                case DEFERRED_SYNTAX_ALLOWED_AS_LITERAL:
                    jspPropertyGroup.setDeferredSyntaxAllowedAsLiteral(getElementText(reader, propertyReplacer));
                    break;
                case TRIM_DIRECTIVE_WHITESPACES:
                    jspPropertyGroup.setTrimDirectiveWhitespaces(getElementText(reader, propertyReplacer));
                    break;
                case DEFAULT_CONTENT_TYPE:
                    jspPropertyGroup.setDefaultContentType(getElementText(reader, propertyReplacer));
                    break;
                case BUFFER:
                    jspPropertyGroup.setBuffer(getElementText(reader, propertyReplacer));
                    break;
                case ERROR_ON_UNDECLARED_NAMESPACE:
                    jspPropertyGroup.setErrorOnUndeclaredNamespace(getElementText(reader, propertyReplacer));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return jspPropertyGroup;
    }

}
