package org.ably.circular.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ably.circular.enterprise.EnterpriseResponse;

import java.util.Date;





@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private TransactionStatus status;
    private Date createdAt;
    private Date completedAt;
    private Float quantity;
    private Float price;
    private EnterpriseResponse buyer;
    private EnterpriseResponse seller;
}