package com.movieflix.movie_api.dto;


import java.util.List;

//getters and all-args-constructor
public record MoviePageResponse(List<MovieDto> movieDtos,
                                Integer pageNumber, //at what we are
                                Integer pageSize, // how many pages are there
                                long totalElements, // how many records across all pages
                                int totalPages, //
                                boolean isLast){}
