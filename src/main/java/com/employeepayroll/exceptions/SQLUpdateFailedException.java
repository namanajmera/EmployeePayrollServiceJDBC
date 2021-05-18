package com.employeepayroll.exceptions;

public class SQLUpdateFailedException extends Throwable {
    public SQLUpdateFailedException(String message) {
        super(message);
    }
}
