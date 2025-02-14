package com.movieflix.movie_api.controller;

import com.movieflix.movie_api.auth.dto.ResetPassword;
import com.movieflix.movie_api.auth.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class ResetPasswordController {


    @Autowired
    private ResetPasswordService resetPasswordService;

//    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<String> resetPasswordHandler(@RequestBody ResetPassword resetPassword, @PathVariable String email) {

        return ResponseEntity.ok(resetPasswordService.resetPassword(resetPassword, email));
    }
}
