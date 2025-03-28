package org.ably.circular.enterprise.verification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
@Tag(name = "Enterprise Verification Controller", description = "Enterprise Verification APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VerificationController {

    private final VerificationService verificationService;


    @Operation(summary = "Upload a document for verification")
    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole( 'USER')")
public ResponseEntity<VerificationDocumentResponse> uploadDocument(
        @ModelAttribute VerificationDocumentRequest request) throws IOException {

    VerificationDocumentResponse response = verificationService.uploadDocument(request);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
}



    @Operation(summary = "Get documents for an enterprise")
    @GetMapping("/documents/{enterpriseId}")
    public ResponseEntity<List<VerificationDocumentResponse>> getDocuments(@PathVariable Long enterpriseId) {

        List<VerificationDocumentResponse> documents =
                verificationService.getDocumentsForEnterprise(enterpriseId);

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
}