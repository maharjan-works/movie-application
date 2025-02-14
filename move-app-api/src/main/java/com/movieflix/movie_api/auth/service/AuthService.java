package com.movieflix.movie_api.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.movie_api.auth.model.RefreshToken;
import com.movieflix.movie_api.auth.model.User;
import com.movieflix.movie_api.auth.model.UserRole;
import com.movieflix.movie_api.auth.repository.UserRepository;
import com.movieflix.movie_api.auth.dto.AuthResponse;
import com.movieflix.movie_api.auth.dto.LoginRequest;
import com.movieflix.movie_api.auth.dto.RegisterRequest;

import com.movieflix.movie_api.exception.EmailAlreadyExistsException;
import com.movieflix.movie_api.exception.UsernameAlreadyExistsException;
import com.movieflix.movie_api.exception.SomethingWentWrongException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
//@RequiredArgsConstructor
@Transactional
public class AuthService {


    Logger logger = LoggerFactory.getLogger(AuthService.class);


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


    public AuthResponse register(RegisterRequest request) {
        logger.info("register() started");

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                logger.info("EmailAlreadyExistsException thrown");
                throw new EmailAlreadyExistsException("Email already exists");
            }
            if (userRepository.findByUsername(request.getUsername()).isPresent()){
                logger.info("UsernameAlreadyExistsException thrown");
                throw  new UsernameAlreadyExistsException("Username already exists");
            }

            logger.info("RegisterDto and User model mapped");
            User user = new ObjectMapper().convertValue(request, User.class);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(UserRole.USER);
            var savedUser = userRepository.save(user);
            logger.info("User model saved in db");

            logger.info("Access token generated");
            var accessToken = jwtService.generateToken(savedUser);
            logger.info("refresh token generated");
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

            logger.info("Auth Response returned & register() ended gracefully.");
            return new AuthResponse(
                    accessToken,
                    refreshToken.getRefreshToken(),
                    savedUser.getFirstName(),
                    savedUser.getLastName(),
                    savedUser.getEmail(),
                    savedUser.getUsername()
            );



    }


    public AuthResponse login(LoginRequest request){
        logger.info("login() method started");
        try{
            Authentication authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            logger.info("IsAuthenticated? : "+ authentication.isAuthenticated());
            var user = userRepository.findByEmail(
                    request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found "));
            logger.info("Authenticated Username: " + user.getUsername());
            logger.info("userDetails fetched by email");
            var accessToken = jwtService.generateToken(user);
            logger.info("access token generated");
            var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
            logger.info("refresh token generated");
            logger.info("exited login() method gracefully by returning AuthResponse instance");
            return new AuthResponse(
                    accessToken,
                    refreshToken.getRefreshToken(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getUsername()
            );
        }catch(Exception ex){
            logger.info(ex.getClass().getSimpleName() + " : " + ex.getMessage());
            throw new SomethingWentWrongException(ex.getMessage());
        }
    }


    public String updateUser(User user){
        return "user is updated";
   }
}

//testing angular library
