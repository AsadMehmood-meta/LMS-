package com.itm.LMS.exceptions;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(Long id) {
        super("Employee profile not found with id: " + id);
    }

}
