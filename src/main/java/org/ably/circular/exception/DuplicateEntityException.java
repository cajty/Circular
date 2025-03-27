package org.ably.circular.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntityException extends BaseException {
    public DuplicateEntityException(String entityName, String field ,String value) {
        super(
            String.format("%s already exists with %s: %s", entityName, field, value),
            HttpStatus.CONFLICT,
            "DUPLICATE_ENTITY"
        );
    }
}

