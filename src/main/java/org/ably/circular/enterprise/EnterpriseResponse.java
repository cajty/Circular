package org.ably.circular.enterprise;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnterpriseResponse {
    private Long id;
    private String name;
    private String registrationNumber;
    private EnterpriseType type;
    private VerificationStatus status;
    private Date verifiedAt;
}
