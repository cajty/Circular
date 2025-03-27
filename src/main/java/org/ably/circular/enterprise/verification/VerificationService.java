package org.ably.circular.enterprise.verification;


import java.io.IOException;
import java.util.List;


public interface VerificationService {

     VerificationDocumentResponse save(VerificationDocument verificationDocument);

    VerificationDocumentResponse uploadDocument(VerificationDocumentRequest request) throws IOException;


    List<VerificationDocumentResponse> getDocumentsForEnterprise(Long enterpriseId);
}