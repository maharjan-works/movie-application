package com.movieflix.movie_api.controller;

import com.movieflix.movie_api.auth.dto.ChangePassword;
import com.movieflix.movie_api.auth.model.ForgotPassword;
import com.movieflix.movie_api.auth.model.User;
import com.movieflix.movie_api.auth.repository.ForgotPasswordRepository;
import com.movieflix.movie_api.auth.repository.UserRepository;
import com.movieflix.movie_api.dto.MailBody;
import com.movieflix.movie_api.exception.NoVerificationCodeFoundException;
import com.movieflix.movie_api.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    //send email with email verification
    @PostMapping("/verifyEmail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        User user= userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not exists"));
//        logger.info("Full Name: "+ user.getFirstName() + " " + user.getLastName());

        int otp = otpGenerator();

        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 1000*60*30))
                .user(user)
                .build();

        MailBody mailBody = MailBody
                .builder()
                .to(email)
                .text("OTP Password: "+ otp)
                .subject("OTP for forgot password")
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(forgotPassword);

        return ResponseEntity.ok("Verification Code is sent to "+ email);

    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        User user= userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not exists"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user).orElseThrow(() ->  new NoVerificationCodeFoundException("No OTP found to validate"));

        if (fp.getExpirationTime().before(new Date(System.currentTimeMillis()))){
            forgotPasswordRepository.delete(fp);
            return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok("OTP Verified");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email){
        if (!Objects.equals(changePassword.password(), changePassword.confirmPassword())){
            return new ResponseEntity<>("Password mismatch.", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password has been changed");

    }

    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }
}
