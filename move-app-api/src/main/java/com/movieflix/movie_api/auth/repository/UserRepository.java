package com.movieflix.movie_api.auth.repository;

import com.movieflix.movie_api.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String username);
}
