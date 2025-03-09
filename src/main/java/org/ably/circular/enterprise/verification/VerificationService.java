package org.ably.circular.enterprise.verification;

import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.enterprise.EnterpriseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface VerificationService {

     VerificationDocumentResponse save(VerificationDocument verificationDocument);

    VerificationDocumentResponse uploadDocument(VerificationDocumentRequest request) throws IOException;

    void updateVerificationStatus(VerificationStatusUpdateRequest request);

    List<VerificationDocumentResponse> getDocumentsForEnterprise(Long enterpriseId);
}