/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.metadata.merge.javaee.spec;

import org.jboss.metadata.javaee.spec.MessageDestinationReferenceMetaData;
import org.jboss.metadata.javaee.spec.MessageDestinationReferencesMetaData;

/**
 * MessageDestinationReferencesMetaData.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MessageDestinationReferencesMetaDataMerger {
    /**
     * Merge message destination references
     *
     * @param override      the override references
     * @param overriden     the overriden references
     * @param overridenFile the overriden file name
     * @param overrideFile  the override file
     * @return the merged referencees
     */
    public static MessageDestinationReferencesMetaData merge(MessageDestinationReferencesMetaData override,
                                                             MessageDestinationReferencesMetaData overriden, String overridenFile, String overrideFile, boolean mustOverride) {
        if (override == null && overriden == null)
            return null;

        if (override == null)
            return overriden;

        MessageDestinationReferencesMetaData merged = new MessageDestinationReferencesMetaData();
        return merge(merged, overriden, override, "message-destination-ref", overridenFile, overrideFile, mustOverride);
    }

    /**
     * From avaEEMetaDataUtil.java
     */
    private static MessageDestinationReferencesMetaData merge(MessageDestinationReferencesMetaData merged,
                                                              MessageDestinationReferencesMetaData overriden, MessageDestinationReferencesMetaData mapped, String context,
                                                              String overridenFile, String overrideFile, boolean mustOverride) {
        if (merged == null)
            throw new IllegalArgumentException("Null merged");

        // Nothing to do
        if (overriden == null && mapped == null)
            return merged;

        // No override
        if (overriden == null || overriden.isEmpty()) {
            // There are no overrides and no overriden
            // Note: it has been really silly to call upon merge, but allas
            if (mapped == null)
                return merged;

            if (mapped.isEmpty() == false && mustOverride)
                throw new IllegalStateException(overridenFile + " has no " + context + "s but " + overrideFile + " has "
                        + mapped.keySet());
            if (mapped != merged)
                merged.addAll(mapped);
            return merged;
        }

        // Wolf: I want to maintain the order of originals (/ override)
        // Process the originals
        for (MessageDestinationReferenceMetaData original : overriden) {
            String key = original.getKey();
            if (mapped != null && mapped.containsKey(key)) {
                MessageDestinationReferenceMetaData override = mapped.get(key);
                MessageDestinationReferenceMetaData tnew = MessageDestinationReferenceMetaDataMerger.merge(override, original);
                merged.add(tnew);
            } else {
                merged.add(original);
            }
        }

        // Process the remaining overrides
        if (mapped != null) {
            for (MessageDestinationReferenceMetaData override : mapped) {
                String key = override.getKey();
                if (merged.containsKey(key))
                    continue;
                if (mustOverride)
                    throw new IllegalStateException(key + " in " + overrideFile + ", but not in " + overridenFile);
                merged.add(override);
            }
        }

        return merged;
    }

    public static void augment(MessageDestinationReferencesMetaData dest, MessageDestinationReferencesMetaData augment,
                               MessageDestinationReferencesMetaData main, boolean resolveConflicts) {
        for (MessageDestinationReferenceMetaData messageDestinationRef : augment) {
            if (dest.containsKey(messageDestinationRef.getKey())) {
                if (!resolveConflicts && (main == null || !main.containsKey(messageDestinationRef.getKey()))) {
                    throw new IllegalStateException("Unresolved conflict on message destination reference named: "
                            + messageDestinationRef.getKey());
                } else {
                    MessageDestinationReferenceMetaDataMerger.merge(dest.get(messageDestinationRef.getKey()),
                            messageDestinationRef, (main != null) ? main.get(messageDestinationRef.getKey()) : null);
                }
            } else {
                dest.add(messageDestinationRef);
            }
        }
    }

}
