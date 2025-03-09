package org.ably.circular.recyclableMaterial;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialForSale {

    private Long materialId;


    @NotNull(message = "Quantity is required")
   @Min(value = 10, message = "Quantity must be at least 10")
    private Long quantity;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be at least 0")
    private Float price;
}