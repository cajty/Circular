package org.ably.circular.enterprise;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
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
