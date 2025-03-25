package org.ably.circular.enterprise.verification;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationDocumentRequest {
    @NotNull(message = "Enterprise ID is required")
    private Long enterpriseId;

    @NotBlank(message = "Document type is required")
    private String documentType;

    @NotNull(message = "File is required")
@Max(value = 524288, message = "File size must be less than 512KB")
    private MultipartFile file;
}