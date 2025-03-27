package org.ably.circular.exception;

import org.springframework.http.HttpStatus;


public class NotFoundException extends BaseException {
    public NotFoundException(String entityName, Object findByParam) {
        super(
            String.format("%s not found with : %s", entityName, findByParam),
            HttpStatus.NOT_FOUND,
            "NOT_FOUND"
        );
    }
}