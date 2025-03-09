package org.ably.circular.enterprise;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.ably.circular.location.LocationRequest;
import org.ably.circular.location.LocationType;
import org.ably.circular.validation.EnumValue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnterpriseRequest {

    @NotBlank(message = "Enterprise name is required")
    private String name;

    @NotBlank(message = "Registration number is required")
    @Pattern(regexp = "^[A-Z0-9]{5,15}$",
            message = "Registration number must be 5-15 characters long and contain only uppercase letters and numbers")
    private String registrationNumber;

    @NotNull(message = "Enterprise type is required")
    @EnumValue(enumClass = EnterpriseType.class, message = "Invalid enterprise type")
    private EnterpriseType enterpriseType;


}