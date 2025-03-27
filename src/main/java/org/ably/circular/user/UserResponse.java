package org.ably.circular.user;
import lombok.*;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String firstName;
    private String lastName;
   private String phoneNumber;
    private Set<String> roles;
}