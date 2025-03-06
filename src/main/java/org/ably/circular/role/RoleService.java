package org.ably.circular.role;

import java.util.Set;

public interface RoleService {
   Set<Role> getRolesByName(String... names);
    Set<Role> getAllRolesSingleton();
}
