package com.itm.LMS.exceptions;

public class InvalidLeaveStatusException extends RuntimeException {
    public InvalidLeaveStatusException(String message) {
        super(message);
    }
}
