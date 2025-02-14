package com.movieflix.movie_api.exception;

public class PasswordMismatchedException extends RuntimeException{

    public PasswordMismatchedException(String message){
        super(message);
    }
}
