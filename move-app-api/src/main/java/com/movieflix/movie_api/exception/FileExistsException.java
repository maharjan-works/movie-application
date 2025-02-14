package com.movieflix.movie_api.exception;

public class FileExistsException extends RuntimeException{

    public FileExistsException(String message){
        super(message);
    }
}
