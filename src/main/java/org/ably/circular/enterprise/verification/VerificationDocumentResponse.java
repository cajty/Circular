package org.ably.circular.enterprise.verification;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationDocumentResponse {
    private Long id;
    private String documentType;
    private String fileName;
    private String contentType;
     private String filePath;
    private Date uploadedAt;
}
