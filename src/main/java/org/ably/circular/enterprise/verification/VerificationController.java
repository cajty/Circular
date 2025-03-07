package org.ably.circular.enterprise.verification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.ably.circular.security.SecurityUser;
import org.ably.circular.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
@Tag(name = "Verification Controller", description = "Enterprise Verification APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VerificationController {

    private final VerificationService verificationService;
    private final SecurityUser securityUser;

    @Operation(summary = "Upload a document for verification")
    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<VerificationDocumentResponse> uploadDocument(
            @RequestParam("enterpriseId") Long enterpriseId,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) throws IOException {

        User currentUser = securityUser.getLoggedInUser();

        VerificationDocumentResponse response = verificationService.uploadDocument(
                enterpriseId, documentType, file, currentUser.getId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update verification status")
    @PostMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateVerificationStatus(
            @Valid @RequestBody VerificationStatusUpdateRequest request) {

        User currentUser = securityUser.getLoggedInUser();

        verificationService.updateVerificationStatus(request, currentUser.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get documents for an enterprise")
    @GetMapping("/documents/{enterpriseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<VerificationDocumentResponse>> getDocuments(
            @PathVariable Long enterpriseId) {

        List<VerificationDocumentResponse> documents =
                verificationService.getDocumentsForEnterprise(enterpriseId);

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
}