package com.itm.LMS.exceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String username) {
        super("User already exists with username: " + username);
    }
}
