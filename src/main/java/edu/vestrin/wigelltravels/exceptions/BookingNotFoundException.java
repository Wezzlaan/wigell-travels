package edu.vestrin.wigelltravels.exceptions;

public class BookingNotFoundException extends ApiResourceNotFoundException {

    public BookingNotFoundException(Long id) {
        super("Booking with ID: '%d' not found".formatted(id));
    }
}
