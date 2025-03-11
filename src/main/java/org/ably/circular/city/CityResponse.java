package org.ably.circular.city;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {
    private Integer id;
    private String name;
    private Boolean isActive;
    private Date createdAt;
}