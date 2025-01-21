package org.ably.circular.enterprise;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseRequest {

    @NotBlank(message = "Enterprise name is required")
    private String name;

    @NotBlank(message = "Registration number is required")
    @Pattern(regexp = "^[A-Z0-9]{5,15}$",
            message = "Registration number must be 5-15 characters long and contain only uppercase letters and numbers")
    private String registrationNumber;

    @NotNull(message = "Enterprise type is required")
    private EnterpriseType type;
}