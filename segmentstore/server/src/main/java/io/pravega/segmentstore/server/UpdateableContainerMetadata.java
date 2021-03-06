/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.segmentstore.server;

import java.util.Collection;

/**
 * Defines an updateable StreamSegment Metadata.
 */
public interface UpdateableContainerMetadata extends ContainerMetadata, RecoverableMetadata, TruncationMarkerRepository {
    /**
     * Maps a new StreamSegment Name to the given Id.
     *
     * @param streamSegmentName The case-sensitive name of the StreamSegment to map.
     * @param streamSegmentId   The Id of the StreamSegment.
     * @return An UpdateableSegmentMetadata that represents the metadata for the newly mapped StreamSegment.
     */
    UpdateableSegmentMetadata mapStreamSegmentId(String streamSegmentName, long streamSegmentId);

    /**
     * Maps a new StreamSegment to its Parent StreamSegment.
     * This is used for Transactions that are dependent on their parent StreamSegments.
     *
     * @param streamSegmentName     The case-sensitive name of the StreamSegment to map.
     * @param streamSegmentId       The Id of the StreamSegment to map.
     * @param parentStreamSegmentId The Id of the Parent StreamSegment.
     * @return An UpdateableSegmentMetadata that represents the metadata for the newly mapped StreamSegment.
     * @throws IllegalArgumentException If the parentStreamSegmentId refers to an unknown StreamSegment.
     */
    UpdateableSegmentMetadata mapStreamSegmentId(String streamSegmentName, long streamSegmentId, long parentStreamSegmentId);

    /**
     * Marks the StreamSegment and all child StreamSegments as deleted.
     *
     * @param streamSegmentName The name of the StreamSegment to delete.
     * @return A Collection of SegmentMetadatas for the Segments that have been deleted. This includes the given StreamSegment,
     * as well as any child StreamSegments that have been deleted.
     */
    Collection<SegmentMetadata> deleteStreamSegment(String streamSegmentName);

    /**
     * Gets the next available Operation Sequence Number. Atomically increments the value by 1 with every call.
     *
     * @return The next available Operation Sequence Number.
     * @throws IllegalStateException If the Metadata is in Recovery Mode.
     */
    long nextOperationSequenceNumber();

    /**
     * Gets the StreamSegmentMetadata mapped to the given StreamSegment Id.
     *
     * @param streamSegmentId The Id of the StreamSegment to query for.
     * @return The mapped StreamSegmentMetadata, or null if none is.
     */
    @Override
    UpdateableSegmentMetadata getStreamSegmentMetadata(long streamSegmentId);
}
