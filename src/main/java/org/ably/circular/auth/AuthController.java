package org.ably.circular.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ably.circular.user.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Auth Controller", description = "Authentication APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthServiceImpl authService;




    @Operation(summary = "Register a new user")
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> register(@Valid  @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }
    @Operation(summary = "Authenticate user")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginUserDto) {
        try {
            return ResponseEntity.ok(authService.authenticate(loginUserDto));
        } catch (Exception e) {

            e.printStackTrace();


            return ResponseEntity
                     .status(HttpStatus.UNAUTHORIZED)
                     .body(new LoginResponse());
        }
    }

     @Operation(summary = "get that loging user")
     @GetMapping("/currentUser")
     public  ResponseEntity<UserResponse> getCurrentUser() {
            return ResponseEntity.ok(authService.getCurrentUser());
      }







}
