package com.itm.LMS.exceptions;

public class DuplicateProfileException extends RuntimeException {
    public DuplicateProfileException(Long userId) {
        super("Profile already exists for user id: " + userId);
    }
}
