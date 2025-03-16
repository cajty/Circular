package org.ably.circular.offre;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.recyclableMaterial.Material;
import org.ably.circular.transaction.Transaction;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "offre")
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "")
    private Set<Material> materials;

    @ManyToOne
    private Enterprise enterprise;
}
