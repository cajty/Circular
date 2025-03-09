package org.ably.circular.enterprise.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.enterprise.EnterpriseRepository;
import org.ably.circular.enterprise.EnterpriseService;
import org.ably.circular.enterprise.VerificationStatus;
import org.ably.circular.exception.BusinessException;
import org.ably.circular.exception.NotFoundException;
import org.ably.circular.security.CurrentUserProvider;
import org.ably.circular.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private final UserService userService;

    private final String UPLOAD_DIR = "uploads/verification-documents/";


    @Override
    public VerificationDocumentResponse save(VerificationDocument document) {
       return  verificationMapper.toResponse(verificationDocumentRepository.save(document));

    }

    @Override
    public VerificationDocumentResponse uploadDocument(VerificationDocumentRequest request) throws IOException {
        log.info("Uploading document for enterprise ID: {}",request.getEnterpriseId());


        Enterprise enterprise = enterpriseService.findEntityById(request.getEnterpriseId());





        Path uploadPath = Paths.get(UPLOAD_DIR + request.getEnterpriseId());
        Files.createDirectories(uploadPath);


        String originalFilename = request.getFile().getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID() + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFilename);


        Files.copy(request.getFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        VerificationDocument document = new VerificationDocument();
         UUID useId = currentUserProvider.getCurrentUserIdOrThrow();
         document.setUploadedBy(useId);
        document.setEnterprise(enterprise);
        document.setDocumentType(request.getDocumentType());
        document.setFileName(originalFilename);
        document.setContentType(request.getFile().getContentType());
        document.setFilePath(filePath.toString());
        document.setUploadedAt(new Date());



        if (enterprise.getStatus() == VerificationStatus.PENDING) {
            enterprise.setStatus(VerificationStatus.UNDER_REVIEW);
            enterpriseRepository.save(enterprise);
        }

        return save(document);
    }

    @Override
    public void updateVerificationStatus(VerificationStatusUpdateRequest request) {

    }




    @Override
    @Transactional(readOnly = true)
    public List<VerificationDocumentResponse> getDocumentsForEnterprise(Long enterpriseId) {
        log.debug("Getting documents for enterprise ID: {}", enterpriseId);

        // Check if enterprise exists
        if (!enterpriseRepository.existsById(enterpriseId)) {
            throw new NotFoundException("Enterprise", enterpriseId);
        }

        List<VerificationDocument> documents = verificationDocumentRepository.findByEnterpriseId(enterpriseId);

        return verificationMapper.toResponseList(documents);
    }


    private boolean isValidStatusTransition(VerificationStatus currentStatus, VerificationStatus newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false;
        }

        switch (currentStatus) {
            case PENDING:
                return newStatus == VerificationStatus.UNDER_REVIEW || newStatus == VerificationStatus.REJECTED;

            case UNDER_REVIEW:
                return newStatus == VerificationStatus.VERIFIED || newStatus == VerificationStatus.REJECTED;

            case VERIFIED:
                return newStatus == VerificationStatus.REJECTED; // Can revoke verification

            case REJECTED:
                return newStatus == VerificationStatus.PENDING; // Can reapply

            default:
                return false;
        }
    }
}