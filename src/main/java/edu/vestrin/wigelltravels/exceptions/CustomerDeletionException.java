package edu.vestrin.wigelltravels.exceptions;

public class CustomerDeletionException extends IllegalArgumentException{

    public CustomerDeletionException(String message) {
        super(message);
    }
}
