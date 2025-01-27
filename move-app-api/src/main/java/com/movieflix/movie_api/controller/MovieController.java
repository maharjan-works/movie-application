package com.movieflix.movie_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.movie_api.dto.MovieDto;
import com.movieflix.movie_api.exception.EmptyFileException;
import com.movieflix.movie_api.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/add")
    public ResponseEntity<MovieDto> addMovie(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException {

        if(file.isEmpty()){
            throw new EmptyFileException("file is empty. please select a file.");
        }
        MovieDto dto = convertToMovieDto(movieDto);
        return new  ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable Integer id){
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getMovies(){
        return ResponseEntity.ok(movieService.getMovies());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer id,
                                                       @RequestPart MultipartFile file,
                                                       @RequestPart String movieDtoStr
    ) throws IOException {
        if (file.isEmpty()) file = null;
        return ResponseEntity.ok(movieService.updateMovie(id,convertToMovieDto(movieDtoStr) ,file));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(id));
    }


    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, MovieDto.class);
    }


}
