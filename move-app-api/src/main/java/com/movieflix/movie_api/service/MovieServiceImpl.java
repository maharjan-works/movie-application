package com.movieflix.movie_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.movie_api.dto.MovieDto;
import com.movieflix.movie_api.dto.MoviePageResponse;
import com.movieflix.movie_api.exception.FileExistsException;
import com.movieflix.movie_api.exception.MovieNotFoundException;
import com.movieflix.movie_api.model.Movie;
import com.movieflix.movie_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistsException("file already existed. Enter another file name");
        }
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

        //we cannot use object mapper here because posterUrl is unknown field to Movie class.
//        ObjectMapper objectMapper = new ObjectMapper();
//        Movie movie = objectMapper.convertValue(movieDto, Movie.class);

        //4. save the movie obj -> saved move obj
        Movie savedMovie = movieRepository.save(movie);

        //5. generate the posterUrl
        String posterUrl = baseUrl +"/file/"+uploadedFileName;

        //6. map Movie obj to dto obj and return it
//        MovieDto response = new MovieDto(
//                null,
//                savedMovie.getTitle(),
//                savedMovie.getDirector(),
//                savedMovie.getStudio(),
//                savedMovie.getMovieCast(),
//                savedMovie.getReleaseYear(),
//                savedMovie.getPoster(),
//                posterUrl
//        );

        ObjectMapper objectMapper = new ObjectMapper();
        MovieDto  response = objectMapper.convertValue(movie, MovieDto.class);
        response.setPosterUrl(posterUrl);
        return response;
    }

    @Override
    public MovieDto getMovie(Integer id) {
        // 1. check the data in DB and if exists, fetch data of given id
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("movie not found"));


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
            MovieDto mDto = new ObjectMapper().convertValue(movie, MovieDto.class);
            mDto.setPosterUrl(posterUrl);
            response.add(mDto);
        }
        return response;
    }

    @Override
    public MovieDto updateMovie(Integer id, MovieDto movieDto, MultipartFile file) throws IOException {
        //1. check if movie object exists with given id;
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("movie not found with id: "+ id));

        //2. if file is null, do nothing
        // if file is not null, the delete existing file associated with the record
        // amd upload the new  file
        String fileName = movie.getPoster();
        if(file != null){
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path,file);
        }

        //3. set movieDto's poster value according to step 2
        movieDto.setPoster(fileName);

        //4. map it to Movie Object=
        movie.setTitle(movieDto.getTitle());
        movie.setDirector(movieDto.getDirector());
        movie.setStudio(movieDto.getStudio());
        movie.setMovieCast(movieDto.getMovieCast());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setPoster(movieDto.getPoster());

        //5. save the movie object -> return saved movie object
        Movie savedMovie = movieRepository.save(movie);

        // 6. generate posterUrl for it
        String posterUrl = baseUrl+"/file/"+fileName;

        // 7. map to movieDto and return it ;
        return new MovieDto(
                savedMovie.getId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
    }

    @Override
    public String deleteMovie(Integer id) throws IOException {
        // 1. check if record exists
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("movie not found"));

        // 2. delete the file
            Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));
        //3. delete movie object
        movieRepository.deleteById(id);

        return  "movie deleted with id: "+ id;
    }

    @Override
    public MoviePageResponse getMoviesWithPagination(Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable); // all movies from all pages
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto mDto = new ObjectMapper().convertValue(movie, MovieDto.class);
            mDto.setPosterUrl(posterUrl);
            movieDtos.add(mDto);
        }

        return new MoviePageResponse(
                movieDtos,
                pageNumber,
                pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast()
        );
    }

    @Override
    public MoviePageResponse getMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> moviePages = movieRepository.findAll(pageable); // all movies from all pages
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto mDto = new ObjectMapper().convertValue(movie, MovieDto.class);
            mDto.setPosterUrl(posterUrl);
            movieDtos.add(mDto);
        }
        return new MoviePageResponse(
                movieDtos, pageNumber,pageSize,
                moviePages.getTotalElements(),moviePages.getTotalPages(),moviePages.isLast()
        );
    }
}
