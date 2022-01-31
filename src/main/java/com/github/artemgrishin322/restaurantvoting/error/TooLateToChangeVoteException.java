package com.github.artemgrishin322.restaurantvoting.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

public class TooLateToChangeVoteException extends AppException {
    public TooLateToChangeVoteException(String message) {
        super(HttpStatus.CONFLICT, message, ErrorAttributeOptions.of(MESSAGE));
    }
}
