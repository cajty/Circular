package org.ably.circular.location;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveLocationResponse {
    private Long id;
    private String address;
    private String cityName;
}
