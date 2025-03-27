package org.ably.circular.enterprise.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ably.circular.aws.StorageService;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.enterprise.EnterpriseRepository;
import org.ably.circular.enterprise.EnterpriseService;
import org.ably.circular.enterprise.VerificationStatus;
import org.ably.circular.exception.BusinessException;
import org.ably.circular.security.CurrentUserProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseService enterpriseService;
    private final VerificationDocumentRepository verificationDocumentRepository;
    private final VerificationMapper verificationMapper;
    private final CurrentUserProvider currentUserProvider;
    private final StorageService storageService;





    @Override
    public VerificationDocumentResponse save(VerificationDocument document) {
       return  verificationMapper.toResponse(
               verificationDocumentRepository.save(document)
       );

    }

    @Override
    public VerificationDocumentResponse uploadDocument(VerificationDocumentRequest request) throws IOException {
        log.info("Uploading document for enterprise ID: {}",request.getEnterpriseId());

        Enterprise enterprise = enterpriseService.findEntityById(request.getEnterpriseId());

          if (!(enterprise.getStatus() == VerificationStatus.PENDING || enterprise.getStatus() == VerificationStatus.UNDER_REVIEW)) {
            enterpriseRepository.save(enterprise);
                throw new BusinessException("Enterprise status is not valid for document upload", HttpStatus.BAD_REQUEST);
        }


       enterprise.setStatus(VerificationStatus.UNDER_REVIEW);
       UUID useId = currentUserProvider.getCurrentUserIdOrThrow();
       VerificationDocument document = VerificationDocument.builder()
               .uploadedBy(useId)
               .enterprise(enterprise)
               .fileName(request.getFile().getOriginalFilename())
               .filePath(storageService.uploadFile(request.getFile()))
               .documentType(request.getDocumentType())
               .contentType(request.getFile().getContentType())
               .uploadedAt(new Date())
               .build();


        return save(document);
    }




    @Override
    @Transactional(readOnly = true)
    public List<VerificationDocumentResponse> getDocumentsForEnterprise(Long enterpriseId) {

        List<VerificationDocument> documents = verificationDocumentRepository.findByEnterpriseId(enterpriseId);

        return verificationMapper.toResponseList(documents);
    }

}