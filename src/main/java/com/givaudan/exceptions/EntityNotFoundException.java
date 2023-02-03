package com.givaudan.exceptions;

public class EntityNotFoundException extends RuntimeException {
    private String message;

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundException(String msg) {
        super(msg);
        this.message = msg;
    }


    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }


}
