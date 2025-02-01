package com.movieflix.movie_api.exception;

public class RefreshTokenAlreadyExpiredException extends RuntimeException{

    public RefreshTokenAlreadyExpiredException(String message){
        super(message);
    }
}
