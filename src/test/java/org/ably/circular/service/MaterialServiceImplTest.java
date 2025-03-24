package org.ably.circular.service;


import org.ably.circular.MaterialCategory.Category;
import org.ably.circular.MaterialCategory.CategoryService;
import org.ably.circular.exception.BusinessException;
import org.ably.circular.exception.NotFoundException;
import org.ably.circular.location.Location;
import org.ably.circular.location.LocationService;
import org.ably.circular.recyclableMaterial.*;
import org.ably.circular.security.CurrentUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceImplTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private MaterialMapper materialMapper;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private CategoryService categoryService;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private MaterialServiceImpl materialService;

    private Material material;
    private MaterialRequest materialRequest;
    private MaterialResponse materialResponse;
    private Category category;
    private Location location;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        // Setup future date for availableUntil
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        futureDate = calendar.getTime();

        // Setup test data
        category = Category.builder()
                .id(1L)
                .name("Plastic")
                .isActive(true)
                .build();

        location = Location.builder()
                .id(1L)
                .address("123 Test St")
                .isActive(true)
                .build();

        material = Material.builder()
                .id(1L)
                .name("Recycled Plastic")
                .description("High quality recycled plastic")
                .quantity(100L)
                .price(50f)
                .status(MaterialStatus.AVAILABLE)
                .hazardLevel(HazardLevel.LOW)
                .unit(MaterialUnit.KG)
                .category(category)
                .location(location)
                .availableUntil(futureDate)
                .build();

        materialRequest = MaterialRequest.builder()
                .name("Recycled Plastic")
                .description("High quality recycled plastic")
                .quantity(100L)
                .price(50f)
                .status(MaterialStatus.AVAILABLE)
                .hazardLevel(HazardLevel.LOW)
                .unit(MaterialUnit.KG)
                .categoryId(1L)
                .locationId(1L)
                .availableUntil(futureDate)
                .build();

        materialResponse = MaterialResponse.builder()
                .id(1L)
                .name("Recycled Plastic")
                .description("High quality recycled plastic")
                .quantity(100L)
                .price(50f)
                .status(MaterialStatus.AVAILABLE)
                .hazardLevel(HazardLevel.LOW)
                .build();
    }

    @Test
    void findById_WhenMaterialExists_ShouldReturnMaterial() {
        // Arrange
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        when(materialMapper.toResponse(material)).thenReturn(materialResponse);

        // Act
        MaterialResponse result = materialService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Recycled Plastic", result.getName());
        assertEquals(MaterialStatus.AVAILABLE, result.getStatus());
        verify(materialRepository).findById(1L);
        verify(materialMapper).toResponse(material);
    }

    @Test
    void findById_WhenMaterialDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> materialService.findById(1L));
        verify(materialRepository).findById(1L);
        verifyNoInteractions(materialMapper);
    }

    @Test
    void create_WithValidRequest_ShouldCreateMaterial() {
        // Arrange
        doNothing().when(categoryService).existsById(1L);
        doNothing().when(locationService).existsById(1L);
        when(materialMapper.toEntity(materialRequest)).thenReturn(material);
        when(materialRepository.save(material)).thenReturn(material);
        when(materialMapper.toResponse(material)).thenReturn(materialResponse);

        // Act
        MaterialResponse result = materialService.create(materialRequest);

        // Assert
        assertNotNull(result);
        assertEquals(materialResponse, result);
        assertEquals(MaterialStatus.RESERVED, material.getStatus()); // Status should be updated to RESERVED
        verify(categoryService).existsById(1L);
        verify(locationService).existsById(1L);
        verify(materialMapper).toEntity(materialRequest);
        verify(materialRepository).save(material);
        verify(materialMapper).toResponse(material);
    }

    @Test
    void create_WithInvalidCategoryId_ShouldThrowBusinessException() {
        // Arrange
        doThrow(new NotFoundException("Category", 1L)).when(categoryService).existsById(1L);

        // Act & Assert
        assertThrows(BusinessException.class, () -> materialService.create(materialRequest));
        verify(categoryService).existsById(1L);
        verifyNoInteractions(materialMapper, materialRepository);
    }

    @Test
    void create_WithInvalidLocationId_ShouldThrowBusinessException() {
        // Arrange
        doNothing().when(categoryService).existsById(1L);
        doThrow(new NotFoundException("Location", 1L)).when(locationService).existsById(1L);

        // Act & Assert
        assertThrows(BusinessException.class, () -> materialService.create(materialRequest));
        verify(categoryService).existsById(1L);
        verify(locationService).existsById(1L);
        verifyNoInteractions(materialMapper, materialRepository);
    }

    @Test
    void create_WithPastAvailableUntilDate_ShouldThrowBusinessException() {
        // Arrange
        // Create a date in the past
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date pastDate = calendar.getTime();

        // Create request with past date
        MaterialRequest requestWithPastDate = MaterialRequest.builder()
                .name("Recycled Plastic")
                .quantity(100L)
                .price(50f)
                .status(MaterialStatus.AVAILABLE)
                .hazardLevel(HazardLevel.LOW)
                .unit(MaterialUnit.KG)
                .categoryId(1L)
                .locationId(1L)
                .availableUntil(pastDate)
                .build();

        // Act & Assert
        assertThrows(BusinessException.class, () -> materialService.create(requestWithPastDate));
    }

//    @Test
//    void update_WithValidRequest_ShouldUpdateMaterial() {
//        // Arrange
//        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
//        doNothing().when(categoryService).existsById(1L);
//        doNothing().when(locationService).existsById(1L);
//        when(materialRepository.save(material)).thenReturn(material);
//        when(materialMapper.toResponse(material)).thenReturn(materialResponse);
//
//        // Act
//        MaterialResponse result = materialService.update(1L, materialRequest);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(materialResponse, result);
//        verify(materialRepository).findById(1L);
//        verify(categoryService).existsById(1L);
//        verify(locationService).existsById(1L);
//        verify(materialMapper).updateEntityFromRequest(materialRequest, material);
//        verify(materialMapper).toResponse(material);
//    }

    @Test
    void delete_WhenMaterialIsAvailable_ShouldDeleteMaterial() {
        // Arrange
        material.setStatus(MaterialStatus.AVAILABLE);
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        // Act
        materialService.delete(1L);

        // Assert
        verify(materialRepository).findById(1L);
        verify(materialRepository).deleteById(1L);
    }

    @Test
    void delete_WhenMaterialIsNotAvailable_ShouldThrowBusinessException() {
        // Arrange
        material.setStatus(MaterialStatus.RESERVED);
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        // Act & Assert
        assertThrows(BusinessException.class, () -> materialService.delete(1L));
        verify(materialRepository).findById(1L);
        verify(materialRepository, never()).deleteById(anyLong());
    }

    @Test
    void findAll_ShouldReturnPageOfMaterials() {
        // Arrange
        Page<Material> materialPage = new PageImpl<>(Collections.singletonList(material));
        Pageable pageable = PageRequest.of(0, 10);

        when(materialRepository.findAll(pageable)).thenReturn(materialPage);
        when(materialMapper.toResponse(material)).thenReturn(materialResponse);

        // Act
        Page<MaterialResponse> result = materialService.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(materialResponse, result.getContent().get(0));
        verify(materialRepository).findAll(pageable);
        verify(materialMapper).toResponse(material);
    }

    @Test
    void search_WithValidParameters_ShouldReturnFilteredResults() {
        // Arrange
        Page<Material> materialPage = new PageImpl<>(Collections.singletonList(material));
        Pageable pageable = PageRequest.of(0, 10);
        String name = "Plastic";
        Float minPrice = 30f;
        Float maxPrice = 100f;
        MaterialStatus status = MaterialStatus.AVAILABLE;

        when(materialRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(materialPage);
        when(materialMapper.toResponse(material)).thenReturn(materialResponse);

        // Act
        Page<MaterialResponse> result = materialService.search(name, minPrice, maxPrice, status, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(materialResponse, result.getContent().get(0));
        verify(materialRepository).findAll(any(Specification.class), eq(pageable));
        verify(materialMapper).toResponse(material);
    }
}
