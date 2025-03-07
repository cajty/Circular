package org.ably.circular.enterprise.verification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocumentResponse {
    private Long id;
    private String documentType;
    private String fileName;
    private String contentType;
    private Date uploadedAt;
    private UUID uploadedBy;
    private Boolean isVerified;
}
