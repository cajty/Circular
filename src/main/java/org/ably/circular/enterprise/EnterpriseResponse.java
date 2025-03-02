package org.ably.circular.enterprise;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnterpriseResponse {
    private Long id;
    private String name;
    private String registrationNumber;
    private EnterpriseType type;
    private VerificationStatus status;
    private Date verifiedAt;
//    private List<LocationDto> locations;
//    private List<UserDto> users;
}
