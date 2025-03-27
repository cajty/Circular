package org.ably.circular.auth;


import org.ably.circular.user.UserResponse;

public interface AuthService {
    LoginResponse authenticate(LoginRequest request);
    LoginResponse signup(RegisterRequest request);
    UserResponse getCurrentUser();
}