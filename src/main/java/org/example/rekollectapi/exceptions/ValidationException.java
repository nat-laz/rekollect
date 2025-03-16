package org.example.rekollectapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422 Unprocessable Content
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
