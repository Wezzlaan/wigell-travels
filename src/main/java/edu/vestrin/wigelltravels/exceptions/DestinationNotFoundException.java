package edu.vestrin.wigelltravels.exceptions;

public class DestinationNotFoundException extends ApiResourceNotFoundException{
    public DestinationNotFoundException(Long id) {
        super("Destination with ID: '%d' not found".formatted(id));
    }
}
