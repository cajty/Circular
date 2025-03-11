package org.ably.circular.location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ably.circular.city.City;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.recyclableMaterial.Material;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "locations")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationType type;


    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @OneToMany(mappedBy = "location")
    private List<Material> materials;

    private Timestamp deletedAt;
}