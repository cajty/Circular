package org.ably.circular.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.recyclableMaterial.Material;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")

@SQLDelete(sql = "UPDATE transactions SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
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

     @Temporal(TemporalType.TIMESTAMP)
   private Date deletedAt;
}