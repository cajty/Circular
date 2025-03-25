package org.ably.circular.service;

import org.ably.circular.enterprise.*;
import org.ably.circular.exception.BusinessException;
import org.ably.circular.exception.NotFoundException;
import org.ably.circular.location.LocationRepository;
import org.ably.circular.role.Role;
import org.ably.circular.role.RoleService;
import org.ably.circular.security.CurrentUserProvider;
import org.ably.circular.user.User;
import org.ably.circular.user.UserService;
import org.ably.circular.user.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnterpriseServiceImplTest {

    @Mock
    private EnterpriseRepository enterpriseRepository;

    @Mock
    private EnterpriseMapper enterpriseMapper;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private EnterpriseServiceImpl enterpriseService;

    private Enterprise enterprise;
    private EnterpriseRequest enterpriseRequest;
    private EnterpriseResponse enterpriseResponse;
    private User user;
    private Set<Role> managerRoles;

    @BeforeEach
    void setUp() {
        // Setup test data
        enterprise = Enterprise.builder()
                .id(1L)
                .name("Test Enterprise")
                .registrationNumber("TEST12345")
                .type(EnterpriseType.RECYCLER)
                .status(VerificationStatus.PENDING)
                .build();

        enterpriseRequest = new EnterpriseRequest(
                "Test Enterprise",
                "TEST12345",
                EnterpriseType.RECYCLER
        );

        enterpriseResponse = EnterpriseResponse.builder()
                .id(1L)
                .name("Test Enterprise")
                .registrationNumber("TEST12345")
                .type(EnterpriseType.RECYCLER)
                .status(VerificationStatus.PENDING)
                .build();

        user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .status(UserStatus.ACTIVE)
                .build();

        Role managerRole = new Role(1, "MANAGER", null);
        Role userRole = new Role(2, "USER", null);
        managerRoles = new HashSet<>(Arrays.asList(managerRole, userRole));
    }

    @Test
    @DisplayName("findById: When Enterprise Does Not Exist, Should Throw NotFoundException")
    void findById_WhenEnterpriseExists_ShouldReturnEnterprise() {
        // Arrange
        when(enterpriseRepository.findById(1L)).thenReturn(Optional.of(enterprise));
        when(enterpriseMapper.toResponse(enterprise)).thenReturn(enterpriseResponse);

        // Act
        EnterpriseResponse result = enterpriseService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Enterprise", result.getName());
        assertEquals("TEST12345", result.getRegistrationNumber());
        assertEquals(EnterpriseType.RECYCLER, result.getType());
        assertEquals(VerificationStatus.PENDING, result.getStatus());
        verify(enterpriseRepository).findById(1L);
        verify(enterpriseMapper).toResponse(enterprise);
    }

    @Test
    @DisplayName("create: With Valid Request, Should Create Enterprise")
    void findById_WhenEnterpriseDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(enterpriseRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> enterpriseService.findById(1L));
        verify(enterpriseRepository).findById(1L);
        verifyNoInteractions(enterpriseMapper);
    }

    @Test
    @DisplayName("create: With Valid Request, Should Create Enterprise")
    void create_WithValidRequest_ShouldCreateEnterprise() {
        // Arrange
        when(enterpriseMapper.toEntity(enterpriseRequest)).thenReturn(enterprise);
        when(currentUserProvider.getCurrentUserOrThrow()).thenReturn(user);
        when(enterpriseRepository.save(any(Enterprise.class))).thenReturn(enterprise);
        when(enterpriseMapper.toResponse(enterprise)).thenReturn(enterpriseResponse);

        // Act
        EnterpriseResponse result = enterpriseService.create(enterpriseRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Enterprise", result.getName());
        assertEquals(EnterpriseType.RECYCLER, result.getType());
        verify(enterpriseMapper).toEntity(enterpriseRequest);
        verify(currentUserProvider).getCurrentUserOrThrow();
        verify(enterpriseRepository).save(any(Enterprise.class));
        verify(enterpriseMapper).toResponse(enterprise);
    }

    @Test
    @DisplayName("create: With Duplicate Registration Number, Should Throw BusinessException")
    void create_WithDuplicateRegistrationNumber_ShouldThrowBusinessException() {
        // Arrange
        when(enterpriseRepository.existsByRegistrationNumber("TEST12345")).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessException.class, () -> enterpriseService.create(enterpriseRequest));
        verify(enterpriseRepository).existsByRegistrationNumber("TEST12345");
        verifyNoInteractions(enterpriseMapper);
        verifyNoInteractions(currentUserProvider);
    }

    @Test
    @DisplayName("update: With Valid Request, Should Update Enterprise")
    void update_WithValidRequest_ShouldUpdateEnterprise() {
        // Arrange
        when(enterpriseRepository.findById(1L)).thenReturn(Optional.of(enterprise));
        when(enterpriseRepository.save(enterprise)).thenReturn(enterprise);
        when(enterpriseMapper.toResponse(enterprise)).thenReturn(enterpriseResponse);

        // Act
        EnterpriseResponse result = enterpriseService.update(1L, enterpriseRequest);

        // Assert
        assertNotNull(result);
        verify(enterpriseRepository).findById(1L);
        verify(enterpriseMapper).updateEntityFromRequest(enterpriseRequest, enterprise);
        verify(enterpriseRepository).save(enterprise);
        verify(enterpriseMapper).toResponse(enterprise);
    }

    @Test
    @DisplayName("delete: When Enterprise Exists, Should Delete Enterprise")
    void delete_WhenEnterpriseExists_ShouldDeleteEnterprise() {
        // Arrange
        when(enterpriseRepository.existsById(1L)).thenReturn(true);

        // Act
        enterpriseService.delete(1L);

        // Assert
        verify(enterpriseRepository).existsById(1L);
        verify(enterpriseRepository).deleteById(1L);
    }

    @Test
    @DisplayName("delete: When Enterprise Does Not Exist, Should Throw NotFoundException")
    void delete_WhenEnterpriseDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(enterpriseRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> enterpriseService.delete(1L));
        verify(enterpriseRepository).existsById(1L);
        verify(enterpriseRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("findAll: Should Return Page Of Enterprises")
    void findAll_ShouldReturnPageOfEnterprises() {
        // Arrange
        List<Enterprise> enterprises = Collections.singletonList(enterprise);
        Page<Enterprise> enterprisePage = new PageImpl<>(enterprises);
        Pageable pageable = PageRequest.of(0, 10);

        when(enterpriseRepository.findAll(pageable)).thenReturn(enterprisePage);
        when(enterpriseMapper.toResponse(enterprise)).thenReturn(enterpriseResponse);

        // Act
        Page<EnterpriseResponse> result = enterpriseService.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(enterpriseResponse, result.getContent().get(0));
        verify(enterpriseRepository).findAll(pageable);
        verify(enterpriseMapper).toResponse(enterprise);
    }

    @Test
    @DisplayName("getEnterpriseOfUser: Should Return Current User Enterprise")
    void getEnterpriseOfUser_ShouldReturnCurrentUserEnterprise() {
        // Arrange
        when(currentUserProvider.getCurrentUserEnterpriseOrThrow()).thenReturn(enterprise);
        when(enterpriseMapper.toResponse(enterprise)).thenReturn(enterpriseResponse);

        // Act
        EnterpriseResponse result = enterpriseService.getEnterpriseOfUser();

        // Assert
        assertNotNull(result);
        assertEquals(enterpriseResponse, result);
        verify(currentUserProvider).getCurrentUserEnterpriseOrThrow();
        verify(enterpriseMapper).toResponse(enterprise);
    }

//    @Test
//    void updateVerificationStatus_ToVerified_ShouldUpdateStatusAndRoles() {
//        // Arrange
//        VerificationStatusUpdateRequest request = new VerificationStatusUpdateRequest(
//                1L,
//                VerificationStatus.VERIFIED,
//                null
//        );
//
//        when(enterpriseRepository.findById(1L)).thenReturn(Optional.of(enterprise));
//        when(userService.findById(any(UUID.class))).thenReturn(user);
//        when(roleService.getRolesByName("MANAGER", "USER")).thenReturn(managerRoles);
//
//        // Act
//        enterpriseService.updateVerificationStatus(request);
//
//        // Assert
//        assertEquals(VerificationStatus.VERIFIED, enterprise.getStatus());
//        assertEquals(managerRoles, user.getRoles());
//        verify(enterpriseRepository).findById(1L);
//        verify(userService).findById(any(UUID.class));
//        verify(roleService).getRolesByName("MANAGER", "USER");
//    }

    @Test
    @DisplayName("updateVerificationStatus: To Rejected, Should Update Status With Reason")
    void updateVerificationStatus_ToRejected_ShouldUpdateStatusWithReason() {
        // Arrange
        String rejectionReason = "Failed verification checks";
        VerificationStatusUpdateRequest request = new VerificationStatusUpdateRequest(
                1L,
                VerificationStatus.REJECTED,
                rejectionReason
        );

        when(enterpriseRepository.findById(1L)).thenReturn(Optional.of(enterprise));

        // Act
        enterpriseService.updateVerificationStatus(request);

        // Assert
        assertEquals(VerificationStatus.REJECTED, enterprise.getStatus());
        assertEquals(rejectionReason, enterprise.getRejectionReason());
        verify(enterpriseRepository).findById(1L);
        verifyNoInteractions(userService);
        verifyNoInteractions(roleService);
    }
}