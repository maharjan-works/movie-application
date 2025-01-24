package com.movieflix.movie_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {


    private Integer id;

    @NotBlank(message="movie title is missing") // application level validation
    private String title;

    @NotBlank(message="director name is missing")
    private String director;

    @NotBlank(message="studio name is missing")
    private String studio;

    @NotBlank(message = "movie cast are missing")
    private Set<String> movieCast;

    private Integer releaseYear;

    @NotBlank(message="poster is missing")
    private String poster;

    @NotBlank(message = "poster url is missing")
    private String posterUrl;

}
