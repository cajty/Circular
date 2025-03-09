package org.ably.circular.enterprise.verification;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ably.circular.enterprise.VerificationStatus;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationStatusUpdateRequest {

    @NotNull(message = "Enterprise ID is required")
    private Long enterpriseId;

    @NotNull(message = "New status is required")
    private VerificationStatus newStatus;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;
}

