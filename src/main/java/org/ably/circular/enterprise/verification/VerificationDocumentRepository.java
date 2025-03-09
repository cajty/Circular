package org.ably.circular.enterprise.verification;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface VerificationDocumentRepository extends JpaRepository<VerificationDocument, Long> {

    List<VerificationDocument> findByEnterpriseId(Long enterpriseId);
    List<VerificationDocument> findByDocumentTypeAndEnterpriseId(String documentType, Long enterpriseId);
}