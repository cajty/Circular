package org.ably.circular.recyclableMaterial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialScheduler {

    private final  MaterialRepository materialRepository;


     @Scheduled(cron = "0 0 0 * * ?")
     @Transactional
     public void updateExpiredMaterials() {
         Date now = new Date();
         int nubOfExpriedMaterials = materialRepository.markMaterialsAsExpired(MaterialStatus.EXPIRED, now);
         log.info("{} materials have been marked as EXPIRED", nubOfExpriedMaterials);
     }
}
