package edu.vestrin.wigelltravels.exceptions;

public class InvalidPhoneNumException extends IllegalArgumentException {
    public InvalidPhoneNumException(String phoneNum) {
        super("Invalid phone number: %s".formatted(phoneNum));
    }
}
