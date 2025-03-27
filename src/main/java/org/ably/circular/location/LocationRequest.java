package org.ably.circular.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.ably.circular.validation.EnumValue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRequest {

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "City ID is required")
    private Long cityId;

    @NotNull(message = "Location type is required")
    @EnumValue(enumClass = LocationType.class, message = "Invalid location type")
    private LocationType type;

    @NotNull(message = "is active is required")
    private Boolean isActive;

}