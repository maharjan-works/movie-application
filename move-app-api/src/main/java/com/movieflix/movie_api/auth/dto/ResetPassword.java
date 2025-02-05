package com.movieflix.movie_api.auth.dto;

public record ResetPassword(String currentPassword, String newPassword, String confirmNewPassword) {
}
