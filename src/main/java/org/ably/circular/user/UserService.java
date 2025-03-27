package org.ably.circular.user;

import java.util.UUID;

public interface UserService {

    User findByEmail(String email);
    User findById(UUID id);
   void delete(UUID id);


}
