package com.movieflix.movie_api.auth.service;

import com.movieflix.movie_api.auth.dto.ResetPassword;
import com.movieflix.movie_api.auth.model.User;
import com.movieflix.movie_api.auth.repository.UserRepository;
import com.movieflix.movie_api.exception.InvalidCurrentPasswordException;
import com.movieflix.movie_api.exception.PasswordMismatchedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ResetPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String resetPassword(ResetPassword resetPassword, String email){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not exists"));


        if(!passwordEncoder.matches(resetPassword.currentPassword(),user.getPassword())){
            throw new InvalidCurrentPasswordException("current password is invalid");
        }
        if (!Objects.equals(resetPassword.newPassword(), resetPassword.confirmNewPassword())){
            throw new PasswordMismatchedException("New Password Mismatch");
        }

        String encodedPassword = passwordEncoder.encode(resetPassword.newPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "password reset successfully";
    }
}
