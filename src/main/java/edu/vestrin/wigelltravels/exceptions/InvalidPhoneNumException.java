package edu.vestrin.wigelltravels.exceptions;

public class InvalidPhoneNumException extends ApiRuntimeException{
    public InvalidPhoneNumException(String phoneNum) {
        super("Invalid phone number: %s".formatted(phoneNum));
    }
}
