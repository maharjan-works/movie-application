package com.movieflix.movie_api.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.movie_api.auth.model.RefreshToken;
import com.movieflix.movie_api.auth.model.User;
import com.movieflix.movie_api.auth.model.UserRole;
import com.movieflix.movie_api.auth.repository.UserRepository;
import com.movieflix.movie_api.auth.dto.AuthResponse;
import com.movieflix.movie_api.auth.dto.LoginRequest;
import com.movieflix.movie_api.auth.dto.RegisterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @Autowired
    private AuthenticationManager authenticationManager;


    public AuthResponse register(RegisterRequest request){
       User  user = new ObjectMapper().convertValue(request, User.class);
       user.setPassword(passwordEncoder.encode(request.getPassword()));
       user.setRole(UserRole.USER);
       var savedUser = userRepository.save(user);

       var accessToken = jwtService.generateToken(savedUser);
       RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

       return new AuthResponse(accessToken,refreshToken.getRefreshToken());
    }


    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(
                request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found ")
        );
        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return new AuthResponse(accessToken,refreshToken.getRefreshToken());
    }
}
