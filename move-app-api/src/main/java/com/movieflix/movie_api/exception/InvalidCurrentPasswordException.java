package com.movieflix.movie_api.exception;

public class InvalidCurrentPasswordException extends RuntimeException{

    public InvalidCurrentPasswordException(String message){
        super(message);
    }
}
