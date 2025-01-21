package org.ably.circular.enterprise;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnterpriseService {

    EnterpriseResponse save(Enterprise Enterprise);

    EnterpriseResponse create(EnterpriseRequest Enterprise);

    EnterpriseResponse update(Long id, EnterpriseRequest Enterprise);

    void delete(Long id);

    EnterpriseResponse findById(Long id);

     Page<EnterpriseResponse> findAll(Pageable pageable);


    void existsById(Long id);





}
