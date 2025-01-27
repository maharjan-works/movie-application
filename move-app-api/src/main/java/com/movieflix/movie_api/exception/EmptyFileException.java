package com.movieflix.movie_api.exception;

public class EmptyFileException extends RuntimeException{

    public EmptyFileException(String message){
        super(message);
    }
}
