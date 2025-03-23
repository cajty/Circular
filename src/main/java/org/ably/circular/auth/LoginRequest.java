package org.ably.circular.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "email is require")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "password is require")
    private String password;


}