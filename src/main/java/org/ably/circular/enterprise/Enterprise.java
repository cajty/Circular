package org.ably.circular.enterprise;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ably.circular.location.Location;
import org.ably.circular.user.User;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enterprises")
public class Enterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @NotBlank
    @Size(min = 5, max = 20)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    private EnterpriseType type;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date verifiedAt;

    @OneToMany(mappedBy = "enterprise")
    private List<User> users;

    @OneToMany(mappedBy = "enterprise")
    private List<Location> locations;

    private Timestamp deletedAt;
}