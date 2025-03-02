package org.ably.circular.recyclableMaterial;

import lombok.RequiredArgsConstructor;


import org.ably.circular.exception.BusinessException;
import org.ably.circular.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;


//    private List<String> validateError(Long id ) {
//
//        List<String> errors = new ArrayList<>();
//      if (fieldRepository.countByFarmId(id) >= 10) {
//    errors.add("Farm can have maximum 5 fields");
//      }
//      if(fieldArea >= farmArea){
//          errors.add("Farm area is full");
//      }
//      if(farmArea /farmArea  <= 0.5){
//          errors.add("Field area is more than 50% of farm area");
//      }
//        return errors;
//    }

//    private void validateMaterialRequest(MaterialRequest request) {
//        if (request == null) {
//            throw new IllegalArgumentException("Material request cannot be null");
//        }
////         List<String> errors = validateError();
//        if (!errors.isEmpty()) {
//            throw new BusinessException(errors.toString(), HttpStatus.BAD_REQUEST);
//        }
//    }


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
//        validateMaterialRequest(request);
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
//        validateMaterialRequest(request);
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



    private Material findEntityById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material", id));
    }


}

