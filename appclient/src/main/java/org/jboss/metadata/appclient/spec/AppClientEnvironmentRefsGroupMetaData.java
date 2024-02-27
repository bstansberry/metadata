/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.metadata.appclient.spec;

import org.jboss.metadata.javaee.spec.MessageDestinationsMetaData;
import org.jboss.metadata.javaee.spec.RemoteEnvironmentRefsGroupMetaData;
import org.jboss.metadata.merge.javaee.spec.RemoteEnvironmentRefsGroupMetaDataMerger;

/**
 * A EarEnvironmentRefsGroupMetaData.
 *
 * @author <a href="alex@jboss.com">Alexey Loubyansky</a>
 * @version $Revision: 1.1 $
 */
public class AppClientEnvironmentRefsGroupMetaData extends RemoteEnvironmentRefsGroupMetaData {
    /**
     * The serialVersionUID
     */
    private static final long serialVersionUID = 8714123546582134095L;

    private MessageDestinationsMetaData messageDestinations;

    public void merge(final AppClientEnvironmentRefsGroupMetaData override, final AppClientEnvironmentRefsGroupMetaData original) {
        RemoteEnvironmentRefsGroupMetaDataMerger.merge(this, override, original, null, null, false);
        if (override != null && override.getMessageDestinations() != null) {
            this.messageDestinations = override.messageDestinations;
        }
    }

    public MessageDestinationsMetaData getMessageDestinations() {
        return messageDestinations;
    }

    public void setMessageDestinations(MessageDestinationsMetaData messageDestinations) {
        this.messageDestinations = messageDestinations;
    }

}
