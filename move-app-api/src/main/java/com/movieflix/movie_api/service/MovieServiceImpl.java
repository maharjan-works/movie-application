package com.movieflix.movie_api.service;

import com.movieflix.movie_api.dto.MovieDto;
import com.movieflix.movie_api.model.Movie;
import com.movieflix.movie_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {

        //1. upload the file
          String uploadedFileName =  fileService.uploadFile(path, file);

        //2. to set the value of field poster as fileName
        movieDto.setPoster(uploadedFileName);

        //3. map dto to Movie obj
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDirector(movieDto.getDirector());
        movie.setStudio(movieDto.getStudio());
        movie.setMovieCast(movieDto.getMovieCast());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setPoster(movieDto.getPoster());

        //4. save the movie obj -> saved move obj
        Movie savedMovie = movieRepository.save(movie);

        //5. generate the posterUrl
        String posterUrl = baseUrl +"/file/"+uploadedFileName;

        //6. map Movie obj to dto obj and return it
        MovieDto response = new MovieDto(
                savedMovie.getId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer id) {
        // 1. check the data in DB and if exists, fetch data of given id
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("movie not found"));


        //2. generate posterUrl
        String posterUrl = baseUrl +"/file/"+ movie.getPoster();

        //map to MovieDto object and return
        MovieDto response = new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public List<MovieDto> getMovies() {
        //1. fetch all data from DB
        List<Movie> movies = movieRepository.findAll();

        //2. iterate through the list, generate posterUrl for each movie object and map to MovieDto obj
        List<MovieDto> response = new ArrayList<>();
        for(Movie movie : movies){
            String posterUrl = baseUrl+"/file/"+ movie.getPoster();
            MovieDto dto = new MovieDto(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            response.add(dto);
        }
        return response;
    }
}
