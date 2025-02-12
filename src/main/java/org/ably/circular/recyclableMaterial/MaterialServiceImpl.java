package org.ably.circular.recyclableMaterial;

import lombok.RequiredArgsConstructor;


import org.ably.circular.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;


    private void validateMaterialRequest(MaterialRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Material request cannot be null");
        }
        //   // to continu
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
        Material updatedMaterial = materialRepository.save(existingMaterial);
        return materialMapper.toResponse(updatedMaterial);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new NotFoundException("Material", id);
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

    @Override
    @Transactional(readOnly = true)
    public void existsById(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new NotFoundException("Material", id);
        }
    }

    private Material findEntityById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material", id));
    }


}

