package org.ably.circular.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ably.circular.recyclableMaterial.MaterialForSale;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Float quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Float price;

    @NotNull(message = "Buyer  is required")
    private Long buyerId;

    private Set<MaterialForSale> materialsForSale;

    @NotNull(message = "Seller  is required")
    private Long sellerId;
}