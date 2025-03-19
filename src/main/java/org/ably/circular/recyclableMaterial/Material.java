package org.ably.circular.recyclableMaterial;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ably.circular.MaterialCategory.Category;
import org.ably.circular.location.Location;


import org.ably.circular.transaction.transactionItem.TransactionItem;
import org.ably.circular.user.User;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "materials")
@SQLDelete(sql = "UPDATE materials SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Long quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialUnit unit;

    private Float price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "listed_at")
    private Date listedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "available_until")
    private Date availableUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HazardLevel hazardLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "material")
    private List<TransactionItem> transactionItems;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
}