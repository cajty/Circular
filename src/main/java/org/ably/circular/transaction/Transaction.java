package org.ably.circular.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.recyclableMaterial.Material;


import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    private Float quantity;




    private Float price;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Enterprise buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Enterprise seller;

    @ManyToMany
    @JoinTable(
        name = "transaction_material",
        joinColumns = @JoinColumn(name = "transaction_id"),
        inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private Set<Material> materials;

    private Timestamp deletedAt;
}