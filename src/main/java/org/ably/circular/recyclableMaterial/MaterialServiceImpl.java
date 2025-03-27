package org.ably.circular.recyclableMaterial;

import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.ably.circular.MaterialCategory.CategoryService;
import org.ably.circular.exception.BusinessException;
import org.ably.circular.exception.NotFoundException;
import org.ably.circular.location.LocationService;
import org.ably.circular.security.CurrentUserProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    private final CurrentUserProvider currentUserProvider;
    private final CategoryService categoryService;
    private final LocationService locationService;

private List<String> validateError(MaterialRequest request) {
    List<String> errors = new ArrayList<>();

    try {
        categoryService.existsById(request.getCategoryId());
    } catch (NotFoundException e) {
        errors.add("Category not found with ID: " + request.getCategoryId());
    }

    try {
        locationService.existsById(request.getLocationId());
    } catch (NotFoundException e) {
        errors.add("Location not found with ID: " + request.getLocationId());
    }


    if (request.getAvailableUntil() != null) {
        Date now = new Date();
        if (request.getAvailableUntil().before(now) && request.getStatus() != MaterialStatus.EXPIRED) {
            errors.add("Available until date cannot be in the past");
        }

    }

    return errors;
}

private void validateMaterialRequest(MaterialRequest request) {
    if (request == null) {
        throw new IllegalArgumentException("Material request cannot be null");
    }

    List<String> errors = validateError(request);
    if (!errors.isEmpty()) {
        throw new BusinessException(String.join(", ", errors), HttpStatus.BAD_REQUEST);
    }
}


    @Override
    @Transactional(readOnly = true)
    public void existsById(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new NotFoundException("Material", id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialResponse> search(String name, Float minPrice, Float maxPrice, MaterialStatus status, Pageable pageable) {
             return materialRepository.findAll(
                     MaterialSpecification.filterByCriteria(name, minPrice, maxPrice, status), pageable
                     )
                .map(materialMapper::toResponse);
    }

    @Override
    @Transactional
    public MaterialResponse save(Material material) {
        Material savedMaterial = materialRepository.save(material);
        return materialMapper.toResponse(savedMaterial);
    }

    @Override
    @Transactional
    public MaterialResponse create(MaterialRequest request) {
    validateMaterialRequest(request);
        Material material = materialMapper.toEntity(request);
        material.setStatus(MaterialStatus.RESERVED);

        return materialMapper.toResponse(
                materialRepository.save(material)
        );
    }

    @Override
    @Transactional
    public MaterialResponse update(Long id, MaterialRequest request) {
        Material existingMaterial = findEntityById(id);
        validateMaterialRequest(request);
        materialMapper.updateEntityFromRequest(request, existingMaterial);
        return materialMapper.toResponse(existingMaterial);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Material material = findEntityById(id);
       if (material.getStatus() != MaterialStatus.AVAILABLE) {
           throw new BusinessException("Only available materials can be deleted", HttpStatus.BAD_REQUEST);
       }
        materialRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialResponse findById(Long id) {
        Material material = findEntityById(id);
        return materialMapper.toResponse(material);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialResponse> findAll(Pageable pageable) {
        return materialRepository.findAll(pageable)
                .map(materialMapper::toResponse);
    }



    private Material findEntityById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material", id));
    }





}

