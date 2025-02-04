package com.movieflix.movie_api.exception;

public class NoVerificationCodeFoundException extends RuntimeException{

    public NoVerificationCodeFoundException(String message){
        super(message);
    }
}
