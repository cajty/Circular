package org.ably.circular.service;


import org.ably.circular.role.Role;
import org.ably.circular.role.RoleRepository;
import org.ably.circular.role.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role adminRole;
    private Role userRole;
    private Role managerRole;
    private List<Role> allRoles;

    @BeforeEach
    void setUp() {
        adminRole = new Role(1, "ADMIN", null);
        userRole = new Role(2, "USER", null);
        managerRole = new Role(3, "MANAGER", null);
        allRoles = Arrays.asList(adminRole, userRole, managerRole);


        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void getAllRolesSingleton_WhenCacheIsEmpty_ShouldFetchFromRepository() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(allRoles);

        // Act
        Set<Role> result = roleService.getAllRolesSingleton();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(adminRole));
        assertTrue(result.contains(userRole));
        assertTrue(result.contains(managerRole));
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getAllRolesSingleton_WhenCacheIsNotEmpty_ShouldReturnCachedRoles() {
        // Arrange - Setup cached data
        when(roleRepository.findAll()).thenReturn(allRoles);
        roleService.getAllRolesSingleton(); // Cache the roles

        // Reset the mock to verify no more interactions
        reset(roleRepository);

        // Act
        Set<Role> result = roleService.getAllRolesSingleton();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(adminRole));
        assertTrue(result.contains(userRole));
        assertTrue(result.contains(managerRole));

        // Verify roleRepository.findAll() was not called again
        verify(roleRepository, never()).findAll();
    }

    @Test
    void getRolesByName_WhenRolesExist_ShouldReturnMatchingRoles() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(allRoles);
        roleService.getAllRolesSingleton(); // Cache the roles

        // Act
        Set<Role> result = roleService.getRolesByName("ADMIN", "USER");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(adminRole));
        assertTrue(result.contains(userRole));
        assertFalse(result.contains(managerRole));
    }

    @Test
    void getRolesByName_WhenNoRolesMatch_ShouldReturnEmptySet() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(allRoles);
        roleService.getAllRolesSingleton(); // Cache the roles

        // Act
        Set<Role> result = roleService.getRolesByName("NON_EXISTENT_ROLE");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getRolesByName_WhenCacheIsEmpty_ShouldLoadCacheFirst() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(allRoles);

        // Act
        Set<Role> result = roleService.getRolesByName("ADMIN");

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(adminRole));
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getRolesByName_WithMultipleValidRoles_ShouldReturnAllMatchingRoles() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(allRoles);

        // Act
        Set<Role> result = roleService.getRolesByName("ADMIN", "MANAGER", "USER");

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(adminRole));
        assertTrue(result.contains(userRole));
        assertTrue(result.contains(managerRole));
    }

    @Test
    void getRolesByName_WithDuplicateRoleNames_ShouldReturnUniqueRoles() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(allRoles);

        // Act
        Set<Role> result = roleService.getRolesByName("ADMIN", "ADMIN", "USER");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(adminRole));
        assertTrue(result.contains(userRole));
    }

    @Test
    void getRolesByName_WithEmptyArguments_ShouldReturnEmptySet() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(allRoles);

        // Act
        Set<Role> result = roleService.getRolesByName();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getRolesByName_WithMixedCaseNames_ShouldBeCaseSensitive() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(allRoles);

        // Act
        Set<Role> result = roleService.getRolesByName("admin", "USER");

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(userRole));
        assertFalse(result.contains(adminRole));
    }
}
