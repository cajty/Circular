package org.ably.circular.auth;




import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 30)
    private String password;

    @NotBlank(message = "name is required")
    @Size(min = 8, max = 30)
    private String name;


    @NotBlank(message = "Email is required")
    private String email;

    @Max(value = 80)
    @Min(value = 18)
    @NotNull
    private int age;

    @Min(value = 0)
    private Double monthlyIncome;

    @Min(value = 0)
    private int creditScore;



    private Role role = Role.USER;

}