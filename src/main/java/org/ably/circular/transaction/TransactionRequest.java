package org.ably.circular.transaction;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ably.circular.transaction.transactionItem.TransactionItemRequest;


import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {


    private Long buyerId;

    @NotEmpty(message = "Transaction must have at least one item")
    @Valid
    private List<TransactionItemRequest> items;
}