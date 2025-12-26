package com.yt.jpa.hospitalManagement.scheduler;

import com.yt.jpa.hospitalManagement.repository.DoctorSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlotCleanupScheduler {

    private final DoctorSlotRepository doctorSlotRepository;

    // Runs every night at 12 AM
        @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredSlots() {
        int deleted = doctorSlotRepository.deleteByDateBefore(LocalDateTime.now());

        if(deleted > 0){
            log.info("Cleaned {} expired doctor slots", deleted);
        }
    }
}