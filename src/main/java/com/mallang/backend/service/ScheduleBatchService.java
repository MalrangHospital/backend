package com.mallang.backend.service;

import com.mallang.backend.domain.AvailableTime;
import com.mallang.backend.domain.Doctor;
import com.mallang.backend.domain.Schedule;
import com.mallang.backend.repository.DoctorRepository;
import com.mallang.backend.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleBatchService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    public void generateSchedulesForNextMonth(Long doctorId) {
        try {
            // 시작 날짜와 종료 날짜
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(1);

            // 의사 정보 확인
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new IllegalArgumentException("Doctor ID not found: " + doctorId));

            // 날짜별 스케줄 생성
            List<Schedule> schedules = new ArrayList<>();
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                // 월~금요일만 스케줄 생성
                if (date.getDayOfWeek().getValue() >= 1 && date.getDayOfWeek().getValue() <= 5) {
                    // 스케줄 생성
                    Schedule schedule = Schedule.builder()
                            .doctor(doctor)
                            .date(date)
                            .build();

                    // 예약 가능한 시간 생성 및 양방향 관계 설정
                    List<AvailableTime> availableTimes = new ArrayList<>();
                    for (int hour = 9; hour <= 17; hour++) {
                        AvailableTime availableTime = AvailableTime.builder()
                                .time(LocalTime.of(hour, 0))
                                .reserved(false)
                                .schedule(schedule) // 양방향 관계 설정
                                .build();
                        availableTimes.add(availableTime);
                    }
                    schedule.setAvailableTimes(availableTimes);

                    schedules.add(schedule);
                }
            }

            // 데이터베이스에 저장
            scheduleRepository.saveAll(schedules);
            log.info("Schedules generated successfully for doctorId: {}", doctorId);

        } catch (IllegalArgumentException e) {
            log.error("Error generating schedules: {}", e.getMessage(), e);
            throw e; // 클라이언트에 명확한 예외 메시지 전달
        } catch (Exception e) {
            log.error("Unexpected error generating schedules", e);
            throw new RuntimeException("Failed to generate schedules: " + e.getMessage(), e);
        }
    }
}