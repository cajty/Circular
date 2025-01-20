package org.ably.circular.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Authentication APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;





    @Operation
    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerUserDto) {
        if(authService.signup(registerUserDto)) {
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
    }
    @Operation(summary = "Authenticate user")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginUserDto) {
        try {
            return ResponseEntity.ok(authService.authenticate(loginUserDto));
        } catch (Exception e) {

            e.printStackTrace();


            return ResponseEntity
                     .status(HttpStatus.UNAUTHORIZED)
                     .body(new LoginResponse("Unauthorized"));
        }

     




    }


    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("dfegdddd");
    }




}
