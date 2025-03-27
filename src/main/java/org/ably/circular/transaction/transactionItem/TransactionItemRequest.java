package org.ably.circular.transaction.transactionItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionItemRequest {

    @NotNull(message = "Material ID is required")
    private Long materialId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Long quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Float price;
}