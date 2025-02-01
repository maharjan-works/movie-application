package com.movieflix.movie_api.exception;

public class SomethingWentWrongException extends RuntimeException{

    public SomethingWentWrongException(String message){
        super(message);
    }
}
