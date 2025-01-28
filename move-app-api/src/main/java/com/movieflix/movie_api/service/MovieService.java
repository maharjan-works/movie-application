package com.movieflix.movie_api.service;

import com.movieflix.movie_api.dto.MovieDto;
import com.movieflix.movie_api.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;
    MovieDto getMovie(Integer id);
    List<MovieDto> getMovies();
    MovieDto updateMovie(Integer id, MovieDto movieDto, MultipartFile file) throws IOException;
    String deleteMovie(Integer id) throws IOException;

    //pagination and sorting
    MoviePageResponse getMoviesWithPagination(Integer pageNumber, Integer pageSize);
    MoviePageResponse getMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir);

}
