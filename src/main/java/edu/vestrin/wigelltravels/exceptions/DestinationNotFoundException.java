package edu.vestrin.wigelltravels.exceptions;

import com.groupc.shared.exception.ResourceNotFoundException;

public class DestinationNotFoundException extends ResourceNotFoundException {
    public DestinationNotFoundException(Long id) {
        super("Destination with ID: '%d' not found".formatted(id));
    }
}
