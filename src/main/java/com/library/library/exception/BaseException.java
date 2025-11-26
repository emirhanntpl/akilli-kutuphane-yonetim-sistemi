package com.library.library.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final MessageType messageType;
    private final HttpStatus status;

    public BaseException(MessageType messageType, HttpStatus status) {
        super(messageType.getMessage());
        this.messageType = messageType;
        this.status = status;
    }
}
