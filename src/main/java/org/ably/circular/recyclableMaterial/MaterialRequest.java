package org.ably.circular.recyclableMaterial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ably.circular.validation.EnumValue;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequest {

    @NotBlank(message = "Material name is required")
    private String name;

    private String description;

    @NotNull(message = "Quantity is required")
    private Long quantity;

    private Float price;

    @NotNull(message = "Material status is required")
    @EnumValue(enumClass = MaterialStatus.class, message = "Invalid material status")
    private MaterialStatus status;

    @NotNull(message = "Hazard level is required")
    @EnumValue(enumClass = HazardLevel.class, message = "Invalid hazard level")
    private HazardLevel hazardLevel;

    @NotNull(message = "Category ID is required")
    private Long categoryId;



    @NotNull(message = "Location ID is required")
    private Long locationId;
}