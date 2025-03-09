package org.ably.circular.enterprise.verification;


import org.mapstruct.Mapper;


import java.util.List;


@Mapper(componentModel = "spring")
public interface VerificationMapper {


    VerificationDocumentResponse toResponse(VerificationDocument document);

    VerificationDocument toEntity(VerificationDocumentRequest request);

    List<VerificationDocumentResponse> toResponseList(List<VerificationDocument> documents);
}