package edu.vestrin.wigelltravels.exceptions;

import com.groupc.shared.exception.ResourceNotFoundException;

public class BookingNotFoundException extends ResourceNotFoundException {

    public BookingNotFoundException(Long id) {
        super("Booking with ID: '%d' not found".formatted(id));
    }
}
