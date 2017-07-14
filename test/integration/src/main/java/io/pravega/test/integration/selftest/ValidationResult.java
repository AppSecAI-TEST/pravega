/*
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.test.integration.selftest;

import io.pravega.common.Exceptions;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the result of a validation process.
 */
class ValidationResult {
    //region Members
    @Getter
    @Setter
    private ValidationSource source;
    @Getter
    @Setter
    private Duration elapsed;
    @Getter
    @Setter
    private long segmentOffset;

    /**
     * Indicates the length of the validated append. This value is undefined if isSuccess() == false.
     */
    @Getter
    private int length;

    /**
     * Indicates the Routing Key (if applicable) for the validation result.  This value is undefined if isSuccess() == false.
     */
    @Getter
    private int routingKey;

    /**
     * Indicates the failure message. This is undefined if isFailed() == false.
     */
    @Getter
    private String failureMessage;

    //endregion

    //region Constructor

    /**
     * Creates a new instance of the ValidationResult class. Not to be used externally (use the static factory methods instead).
     */
    private ValidationResult() {
        this.length = Append.HEADER_LENGTH;
        this.routingKey = -1;
        this.failureMessage = null;
        this.elapsed = null;
    }

    /**
     * Creates a new ValidationResult for a failed verification.
     */
    static ValidationResult failed(String message) {
        Exceptions.checkNotNullOrEmpty(message, "message");
        ValidationResult result = new ValidationResult();
        result.failureMessage = message;
        return result;
    }

    /**
     * Creates a new ValidationResult for a successful test.
     */
    static ValidationResult success(int routingKey, int length) {
        ValidationResult result = new ValidationResult();
        result.length = Append.HEADER_LENGTH + length;
        result.routingKey = routingKey;
        return result;
    }

    //endregion

    //region Properties

    /**
     * Gets a value indicating whether the verification failed.
     */
    boolean isFailed() {
        return this.failureMessage != null;
    }

    /**
     * Gets a value indicating whether the verification succeeded.
     */
    boolean isSuccess() {
        return !isFailed();
    }

    @Override
    public String toString() {
        if (isFailed()) {
            return String.format("Failed (Source=%s, Offset=%d, Reason=%s)", this.source, this.segmentOffset, this.failureMessage);
        } else {
            return String.format("Success (Source=%s, Offset=%d, Length = %d)", this.source, this.segmentOffset, this.length);
        }
    }

    //endregion
}
