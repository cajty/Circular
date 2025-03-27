package org.ably.circular.location;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponse {
    private Long id;
    private String address;
    private String cityName;
    private LocationType type;
     private Boolean isActive;
}