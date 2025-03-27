package org.ably.circular.enterprise.verification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ably.circular.enterprise.Enterprise;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification_documents")

@SQLDelete(sql = "UPDATE verification_documents SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class VerificationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt;

    @Column(nullable = false)
    private UUID uploadedBy;


    @Temporal(TemporalType.TIMESTAMP)
   private Date deletedAt;
}