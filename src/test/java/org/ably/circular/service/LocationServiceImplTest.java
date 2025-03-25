package org.ably.circular.service;


import org.ably.circular.city.City;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.exception.BusinessException;
import org.ably.circular.exception.NotFoundException;
import org.ably.circular.location.*;
import org.ably.circular.recyclableMaterial.Material;
import org.ably.circular.security.CurrentUserProvider;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location location;
    private LocationRequest locationRequest;
    private LocationResponse locationResponse;
    private Enterprise enterprise;
    private City city;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        city = City.builder()
                .id(1)
                .name("Test City")
                .build();

        enterprise = Enterprise.builder()
                .id(1L)
                .name("Test Enterprise")
                .build();

        location = Location.builder()
                .id(1L)
                .address("123 Test Street")
                .city(city)
                .type(LocationType.WAREHOUSE)
                .isActive(true)
                .enterprise(enterprise)
                .materials(new ArrayList<>())
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setAddress("123 Test Street");
        locationRequest.setCityId(1L);
        locationRequest.setType(LocationType.WAREHOUSE);
        locationRequest.setIsActive(true);

        locationResponse = LocationResponse.builder()
                .id(1L)
                .address("123 Test Street")
                .cityName("Test City")
                .type(LocationType.WAREHOUSE)
                .isActive(true)
                .build();

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Should save location and return location response")
    void save_ShouldSaveLocationAndReturnLocationResponse() {
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(locationMapper.toResponse(any(Location.class))).thenReturn(locationResponse);

        LocationResponse result = locationService.save(location);

        assertNotNull(result);
        assertEquals(locationResponse, result);
        verify(locationRepository).save(location);
        verify(locationMapper).toResponse(location);
    }

    @Test
    @DisplayName("Should create location, set enterprise and return location response")
    void create_ShouldCreateLocationSetEnterpriseAndReturnLocationResponse() {
        when(locationMapper.toEntity(any(LocationRequest.class))).thenReturn(location);
        when(currentUserProvider.getCurrentUserEnterpriseOrThrow()).thenReturn(enterprise);
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(locationMapper.toResponse(any(Location.class))).thenReturn(locationResponse);

        LocationResponse result = locationService.create(locationRequest);

        assertNotNull(result);
        assertEquals(locationResponse, result);
        verify(locationMapper).toEntity(locationRequest);
        verify(currentUserProvider).getCurrentUserEnterpriseOrThrow();
        verify(locationRepository).save(location);
        verify(locationMapper).toResponse(location);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when request is null")
    void create_ShouldThrowIllegalArgumentExceptionWhenRequestIsNull() {
        assertThrows(IllegalArgumentException.class, () -> locationService.create(null));
    }

    @Test
    @DisplayName("Should update location and return location response")
    void update_ShouldUpdateLocationAndReturnLocationResponse() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(locationMapper.toResponse(any(Location.class))).thenReturn(locationResponse);

        LocationResponse result = locationService.update(1L, locationRequest);

        assertNotNull(result);
        assertEquals(locationResponse, result);
        verify(locationRepository).findById(1L);
        verify(locationMapper).updateEntityFromRequest(eq(locationRequest), eq(location));
        verify(locationMapper).toResponse(location);
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existent location")
    void update_ShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> locationService.update(1L, locationRequest));
        verify(locationRepository).findById(1L);
    }

    @Test
    @DisplayName("Should delete location when it has no materials")
    void delete_ShouldDeleteLocationWhenItHasNoMaterials() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));

        locationService.delete(1L);

        verify(locationRepository).findById(1L);
        verify(locationRepository).delete(location);
    }

    @Test
    @DisplayName("Should throw BusinessException when deleting location with materials")
    void delete_ShouldThrowBusinessExceptionWhenLocationHasMaterials() {
        Location locationWithMaterials = location;
        List<Material> materials = new ArrayList<>();
        materials.add(Material.builder().id(1L).build());
        locationWithMaterials.setMaterials(materials);

        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(locationWithMaterials));

        assertThrows(BusinessException.class, () -> locationService.delete(1L));
        verify(locationRepository).findById(1L);
        verify(locationRepository, never()).delete(any(Location.class));
    }

    @Test
    @DisplayName("Should return location response by id")
    void findById_ShouldReturnLocationResponseById() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(locationMapper.toResponse(any(Location.class))).thenReturn(locationResponse);

        LocationResponse result = locationService.findById(1L);

        assertNotNull(result);
        assertEquals(locationResponse, result);
        verify(locationRepository).findById(1L);
        verify(locationMapper).toResponse(location);
    }

    @Test
    @DisplayName("Should throw NotFoundException when finding non-existent location")
    void findById_ShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> locationService.findById(1L));
        verify(locationRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return page of location responses")
    void findAll_ShouldReturnPageOfLocationResponses() {
        Page<Location> locationPage = new PageImpl<>(Collections.singletonList(location));
        when(locationRepository.findAll(any(Pageable.class))).thenReturn(locationPage);
        when(locationMapper.toResponse(any(Location.class))).thenReturn(locationResponse);

        Page<LocationResponse> result = locationService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(locationResponse, result.getContent().get(0));
        verify(locationRepository).findAll(pageable);
        verify(locationMapper).toResponse(location);
    }

    @Test
    @DisplayName("Should throw NotFoundException when location does not exist")
    void existsById_ShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        when(locationRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> locationService.existsById(1L));
        verify(locationRepository).existsById(1L);
    }

    @Test
    @DisplayName("Should not throw exception when location exists")
    void existsById_ShouldNotThrowExceptionWhenIdExists() {
        when(locationRepository.existsById(anyLong())).thenReturn(true);

        locationService.existsById(1L);

        verify(locationRepository).existsById(1L);
    }

    @Test
    @DisplayName("Should return page of location responses for enterprise")
    void getAllLocationOfEnterprise_ShouldReturnPageOfLocationResponsesForEnterprise() {
        Page<Location> locationPage = new PageImpl<>(Collections.singletonList(location));
        when(currentUserProvider.getCurrentUserEnterpriseOrThrow()).thenReturn(enterprise);
        when(locationRepository.findAllByEnterprise_Id(anyLong(), any(Pageable.class))).thenReturn(locationPage);
        when(locationMapper.toResponse(any(Location.class))).thenReturn(locationResponse);

        Page<LocationResponse> result = locationService.getAllLocationOfEnterprise(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(locationResponse, result.getContent().get(0));
        verify(currentUserProvider).getCurrentUserEnterpriseOrThrow();
        verify(locationRepository).findAllByEnterprise_Id(enterprise.getId(), pageable);
        verify(locationMapper).toResponse(location);
    }

    @Test
    @DisplayName("Should return set of active location responses for enterprise")
    void getAllActiveLocationsOfEnterprise_ShouldReturnSetOfActiveLocationResponsesForEnterprise() {
        Set<Location> locations = new HashSet<>(Collections.singletonList(location));
        ActiveLocationResponse activeLocationResponse = ActiveLocationResponse.builder()
                .id(1L)
                .address("123 Test Street")
                .cityName("Test City")
                .build();

        when(currentUserProvider.getCurrentUserEnterpriseOrThrow()).thenReturn(enterprise);
        when(locationRepository.findAllByEnterprise_IdAndIsActiveTrue(anyLong())).thenReturn(locations);
        when(locationMapper.toActiveLocationResponse(any(Location.class))).thenReturn(activeLocationResponse);

        Set<ActiveLocationResponse> result = locationService.getAllActiveLocationsOfEnterprise();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(activeLocationResponse));
        verify(currentUserProvider).getCurrentUserEnterpriseOrThrow();
        verify(locationRepository).findAllByEnterprise_IdAndIsActiveTrue(enterprise.getId());
        verify(locationMapper).toActiveLocationResponse(location);
    }

    @Test
    @DisplayName("Should change location activity status")
    void changeActivityStatus_ShouldChangeLocationActivityStatus() {
        when(locationRepository.existsById(anyLong())).thenReturn(true);

        locationService.changeActivityStatus(1L);

        verify(locationRepository).existsById(1L);
        verify(locationRepository).changeLocationStatus(1L);
    }

    @Test
    @DisplayName("Should throw NotFoundException when changing status of non-existent location")
    void changeActivityStatus_ShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        when(locationRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> locationService.changeActivityStatus(1L));
        verify(locationRepository).existsById(1L);
        verify(locationRepository, never()).changeLocationStatus(anyLong());
    }
}
