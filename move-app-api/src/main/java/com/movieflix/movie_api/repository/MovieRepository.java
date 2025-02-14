package com.movieflix.movie_api.repository;

import com.movieflix.movie_api.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
