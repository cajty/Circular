package org.ably.circular.role;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    // Cache to store all roles, ensuring thread-safe updates
    private final AtomicReference<Set<Role>> allRolesCache = new AtomicReference<>();

    @Override
    @Transactional(readOnly = true)
    @Cacheable("roles")
    public Set<Role> getAllRolesSingleton() {
        Set<Role> roles = allRolesCache.get();
        if (roles == null) {
            roles = Set.copyOf(roleRepository.findAll());
            allRolesCache.set(roles);
        }
        return roles;
    }


    // The String... => varargs" (variable arguments).
    // in Java 5
    // allows to accept a variable number of arguments of the same type creat
   public Set<Role> getRolesByName(String... names) {
       Set<Role> roles = getAllRolesSingleton();
       return roles.stream()
                   .filter(role -> Arrays.asList(names).contains(role.getName()))
                   .collect(Collectors.toSet());
   }


}