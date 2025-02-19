package com.movieflix.movie_api.controller;

import com.movieflix.movie_api.auth.dto.RefreshTokenRequest;
import com.movieflix.movie_api.auth.model.RefreshToken;
import com.movieflix.movie_api.auth.model.User;
import com.movieflix.movie_api.auth.service.AuthService;
import com.movieflix.movie_api.auth.dto.AuthResponse;
import com.movieflix.movie_api.auth.dto.LoginRequest;
import com.movieflix.movie_api.auth.dto.RegisterRequest;
import com.movieflix.movie_api.auth.service.JwtService;
import com.movieflix.movie_api.auth.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/auth")
//@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequest request){
        return ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request){
       RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
       User user = refreshToken.getUser();

       String accessToken = jwtService.generateToken(user);
       return  ok(new AuthResponse(
               accessToken,refreshToken.getRefreshToken(),
               user.getFirstName(),user.getLastName(),user.getEmail(),user.getUsername()
       ));
    }

    @DeleteMapping("/refresh-token/{id}")
    public ResponseEntity<String> deleteRefreshToken(@PathVariable Integer id){
        return ResponseEntity.ok(refreshTokenService.delete(id));
    }




}
