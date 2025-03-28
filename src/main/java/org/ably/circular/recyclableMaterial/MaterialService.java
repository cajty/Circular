package org.ably.circular.recyclableMaterial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaterialService {

    MaterialResponse save(Material material);

    MaterialResponse create(MaterialRequest material);

    MaterialResponse update(Long id, MaterialRequest material);

    void delete(Long id);

    MaterialResponse findById(Long id);

    Page<MaterialResponse> findAll(Pageable pageable);

    void existsById(Long id);

     Page<MaterialResponse> search(String name, Float minPrice, Float maxPrice, MaterialStatus status, Pageable pageable);


 int countAll(String name);
}