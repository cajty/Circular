package org.ably.circular.recyclableMaterial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ably.circular.MaterialCategory.Category;
import org.ably.circular.MaterialCategory.CategoryResponse;
import org.ably.circular.location.ActiveLocationResponse;
import org.ably.circular.location.Location;


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
     private MaterialUnit unit;
    private MaterialStatus status;
    private Date availableUntil;
    private CategoryResponse category;
    private ActiveLocationResponse location;
    private HazardLevel hazardLevel;
}