package com.movieflix.movie_api.auth.service;

import com.movieflix.movie_api.auth.model.RefreshToken;
import com.movieflix.movie_api.auth.model.User;
import com.movieflix.movie_api.auth.repository.RefreshTokenRepository;
import com.movieflix.movie_api.auth.repository.UserRepository;
import com.movieflix.movie_api.exception.RefreshTokenAlreadyExpiredException;
import com.movieflix.movie_api.exception.RefreshTokenNotFoundException;
import com.movieflix.movie_api.exception.SomethingWentWrongException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String username){
       User user =  userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+ username ));
       RefreshToken refreshToken = user.getRefreshToken();

       if (refreshToken == null){
           refreshToken = RefreshToken.builder()
                   .refreshToken(UUID.randomUUID().toString())
                   .expirationTime(Instant.now().plusMillis(1000*60*5)) // 5mins
                   .user(user)
                   .build();
           refreshTokenRepository.save(refreshToken);
       }else if (refreshToken != null && refreshToken.getExpirationTime().isBefore(Instant.now())){
           refreshToken.setExpirationTime(Instant.now().plusMillis(1000*60*60*5)); //5hrs
           refreshTokenRepository.save(refreshToken);
       }
       return refreshToken;
    }
    public RefreshToken verifyRefreshToken(String refreshToken){
        logger.info("verifyRefreshToken() initiated.");
        RefreshToken refToken =  refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
        logger.info("refreshToken found");

//        if(refToken.getExpirationTime().compareTo(Instant.now()) < 0){
        logger.info("checking if refresh token expired?");
          if(refToken.getExpirationTime().isBefore(Instant.now())){
            refreshTokenRepository.delete(refToken);
            logger.info("if refresh token expired, then deleting it from DB and throwing RefreshTokenAlreadyExpiredException");
            throw new RefreshTokenAlreadyExpiredException("Refresh token already expired, so it is deleted.Now login is needed.");
        }
          logger.info("returning refresh token if not expired and exiting verifyRefreshToken() method");
        return refToken;
    }


    public String delete(Integer id){
        try{
           if(refreshTokenRepository.existsById(id)){
               refreshTokenRepository.flush();
               refreshTokenRepository.deleteById(id);
               return "Refresh with Id: "+ id + " deleted";
           }else{
               throw new RefreshTokenNotFoundException("Refresh Token Not Found with id: " + id);
           }
        }catch (Exception ex){
            throw new SomethingWentWrongException("Error: "+ ex.getClass().getSimpleName() + " : "+ ex.getMessage());
        }
    }

}
