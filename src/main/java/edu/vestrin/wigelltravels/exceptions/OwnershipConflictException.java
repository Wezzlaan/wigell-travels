package edu.vestrin.wigelltravels.exceptions;

public class OwnershipConflictException extends IllegalArgumentException {

    public OwnershipConflictException(String message) {
        super(message);
    }
}
