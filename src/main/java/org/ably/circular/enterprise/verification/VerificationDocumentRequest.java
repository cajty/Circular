package org.ably.circular.enterprise.verification;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationDocumentRequest {
    private Long enterpriseId;
    private String documentType;
    private MultipartFile file;
}
