package com.ironhack.demosecurityjwt.security.exceptions;

import javax.management.RuntimeErrorException;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException (String msg) { super(msg);}
}
