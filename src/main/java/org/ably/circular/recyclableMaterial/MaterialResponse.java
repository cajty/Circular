package org.ably.circular.recyclableMaterial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ably.circular.MaterialCategory.Category;
import org.ably.circular.location.Location;
import org.ably.circular.user.User;

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
    private Category category;
    private User user;
    private HazardLevel hazardLevel;
    private Location location;
}