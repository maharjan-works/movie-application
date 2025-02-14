package com.movieflix.movie_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false) // database level validation
    @NotBlank(message="movie title is missing") // application level validation
    private String title;

    @Column(nullable = false)
    @NotBlank(message="director name is missing")
    private String director;

    @Column(nullable = false)
    @NotBlank(message="studio name is missing")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    @Column(nullable=false)
    private Integer releaseYear;

    @Column(nullable=false)
    @NotBlank(message="poster is messing")
    private String poster;
}
