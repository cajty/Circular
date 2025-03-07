package org.ably.circular.enterprise.verification;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface VerificationService {

    VerificationDocumentResponse uploadDocument(Long enterpriseId, String documentType, MultipartFile file, UUID userId)
            throws IOException;

    void updateVerificationStatus(VerificationStatusUpdateRequest request, UUID userId);

    List<VerificationDocumentResponse> getDocumentsForEnterprise(Long enterpriseId);
}