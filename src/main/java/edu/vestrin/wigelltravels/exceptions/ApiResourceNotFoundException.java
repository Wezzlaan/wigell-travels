package edu.vestrin.wigelltravels.exceptions;

import com.groupc.shared.exception.ResourceNotFoundException;

public class ApiResourceNotFoundException extends ResourceNotFoundException {

    public ApiResourceNotFoundException(String message) {
        super(message);
    }
}
