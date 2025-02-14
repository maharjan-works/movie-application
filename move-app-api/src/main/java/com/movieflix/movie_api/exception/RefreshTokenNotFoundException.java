package com.movieflix.movie_api.exception;

public class RefreshTokenNotFoundException extends RuntimeException{

    public RefreshTokenNotFoundException(String message){
        super(message);
    }
}
