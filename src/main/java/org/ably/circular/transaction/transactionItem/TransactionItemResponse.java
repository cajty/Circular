package org.ably.circular.transaction.transactionItem;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ably.circular.recyclableMaterial.MaterialResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionItemResponse {
    private Long id;
    private Long transactionId;
    private MaterialResponse material;
    private Long quantity;
    private Float price;
    private Float totalPrice;
}