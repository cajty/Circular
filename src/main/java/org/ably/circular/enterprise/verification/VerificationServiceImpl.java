package org.ably.circular.enterprise.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.enterprise.EnterpriseRepository;
import org.ably.circular.enterprise.VerificationStatus;
import org.ably.circular.exception.BusinessException;
import org.ably.circular.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VerificationServiceImpl implements VerificationService {

    private final EnterpriseRepository enterpriseRepository;
    private final VerificationDocumentRepository verificationDocumentRepository;
    private final VerificationMapper verificationMapper;

    private final String UPLOAD_DIR = "uploads/verification-documents/";


    public VerificationDocumentResponse uploadDocument(Long enterpriseId, String documentType,
                                                       MultipartFile file, UUID userId) throws IOException {
        log.info("Uploading document for enterprise ID: {}", enterpriseId);


        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new NotFoundException("Enterprise", enterpriseId));


        if (enterprise.getStatus() == VerificationStatus.VERIFIED) {
            throw new BusinessException("Enterprise is already verified");
        }


        Path uploadPath = Paths.get(UPLOAD_DIR + enterpriseId);
        Files.createDirectories(uploadPath);


        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFilename);


        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        VerificationDocument document = new VerificationDocument();
        document.setEnterprise(enterprise);
        document.setDocumentType(documentType);
        document.setFileName(originalFilename);
        document.setContentType(file.getContentType());
        document.setFilePath(filePath.toString());
        document.setUploadedAt(new Date());
        document.setUploadedBy(userId);


        if (enterprise.getStatus() == VerificationStatus.PENDING) {
            enterprise.setStatus(VerificationStatus.UNDER_REVIEW);
            enterpriseRepository.save(enterprise);
        }

        return verificationMapper.toResponse(verificationDocumentRepository.save(document));
    }


    public void updateVerificationStatus(VerificationStatusUpdateRequest request, UUID userId) {
        log.info("Updating verification status for enterprise ID: {} to: {}",
                request.getEnterpriseId(), request.getNewStatus());

        // Find the enterprise
        Enterprise enterprise = enterpriseRepository.findById(request.getEnterpriseId())
                .orElseThrow(() -> new NotFoundException("Enterprise", request.getEnterpriseId()));

        // Check if the transition is valid
        if (!isValidStatusTransition(enterprise.getStatus(), request.getNewStatus())) {
            throw new BusinessException("Invalid status transition from " + enterprise.getStatus() +
                                       " to " + request.getNewStatus());
        }

        // Update enterprise status
        enterprise.setStatus(request.getNewStatus());

        // Set additional fields based on status
        if (request.getNewStatus() == VerificationStatus.VERIFIED) {
            enterprise.setVerifiedAt(new Date());
            enterprise.setVerifiedBy(userId);
        } else if (request.getNewStatus() == VerificationStatus.REJECTED) {
            enterprise.setRejectionReason(request.getReason());
        }

        enterpriseRepository.save(enterprise);
    }

    /**
     * Get verification documents for an enterprise
     */
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

    /**
     * Check if a status transition is valid
     */
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