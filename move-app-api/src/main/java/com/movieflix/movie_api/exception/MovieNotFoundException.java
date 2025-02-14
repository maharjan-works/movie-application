package com.movieflix.movie_api.exception;

public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(String message){
        super(message);
    }
}
