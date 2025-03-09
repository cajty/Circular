package org.ably.circular.recyclableMaterial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponse {
    private Long id;
    private String name;
    private String description;
    private Long quantity;
    private Float price;
    private MaterialStatus status;
    private Date listedAt;
    private Date availableUntil;
    private String categoryName;
    private HazardLevel hazardLevel;
}