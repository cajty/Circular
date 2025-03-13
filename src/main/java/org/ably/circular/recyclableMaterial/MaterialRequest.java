package org.ably.circular.recyclableMaterial;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ably.circular.validation.EnumValue;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequest {

    @NotBlank(message = "Material name is required")
    private String name;

    private String description;

    @NotNull(message = "Quantity is required")
   @Min(value = 10, message = "Quantity must be at least 10")
    private Long quantity;

    @Min(value = 0, message = "Price must be at least 0")
    private Float price;

    @NotNull(message = "Material status is required")
//    @EnumValue(enumClass = MaterialStatus.class, message = "Invalid material status")
    private MaterialStatus status;

    @NotNull(message = "Hazard level is required")
//    @EnumValue(enumClass = HazardLevel.class, message = "Invalid hazard level")
    private HazardLevel hazardLevel;

    @NotNull(message = "Available until date is required")
    private Date availableUntil;

    @NotNull(message = "Category ID is required")
    private Long categoryId;



    @NotNull(message = "Location ID is required")
    private Long locationId;
}