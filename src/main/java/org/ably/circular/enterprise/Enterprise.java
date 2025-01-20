package org.ably.circular.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ably.circular.location.Location;
import org.ably.circular.user.User;

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
}