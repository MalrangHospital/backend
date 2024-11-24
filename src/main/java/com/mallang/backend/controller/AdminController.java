package com.mallang.backend.controller;

import com.mallang.backend.dto.HealthcareReserveDTO;
import com.mallang.backend.domain.Review;
import com.mallang.backend.dto.ReviewDTO;
import com.mallang.backend.service.AdminService;
import com.mallang.backend.service.HealthcareReserveService;
import com.mallang.backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final HealthcareReserveService healthcareReserveService;

    @Autowired
    private AdminService adminService;
    private ReviewService reviewService;

    // HealthcareReserveService를 AdminController에 주입
    public AdminController(HealthcareReserveService healthcareReserveService) {
        this.healthcareReserveService = healthcareReserveService;
    }

    // 관리자 등록
    @PostMapping("/register")
    public String registerAdmin(@RequestParam String adminId, @RequestParam String adminPassword) {
        try {
            adminService.registerAdmin(adminId, adminPassword);
            return "관리자 계정이 등록되었습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // 관리자 인증
    @PostMapping("/login")
    public String authenticateAdmin(@RequestParam String adminId, @RequestParam String adminPassword) {
        try {
            adminService.authenticateAdmin(adminId, adminPassword);
            return "로그인 성공.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // 관리자 삭제
    @DeleteMapping("/{adminName}")
    public String deleteAdmin(@PathVariable String adminName) {
        try {
            adminService.deleteAdmin(adminName);
            return "관리자 계정이 삭제되었습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // 관리자 정보 조회
    @GetMapping("/{adminId}")
    public String getAdminById(@PathVariable String adminId) {
        try {
            return adminService.getAdminById(adminId).toString();
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // 관리자 정보 수정
    @PutMapping("/{adminId}")
    public String updateAdmin(
            @PathVariable String adminId,
            @RequestParam(required = false) String newId,
            @RequestParam(required = false) String newPassword) {
        try {
            adminService.updateAdmin(adminId, newId, newPassword);
            return "관리자 정보가 수정되었습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // 의료진 등록
    @PostMapping("/doctors")
    public String registerDoctor(@RequestParam String name, @RequestParam String specialty, @RequestParam String contact) {
        try {
            adminService.registerDoctor(name, specialty, contact);
            return "의료진 등록이 완료되었습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // 의료진 수정
    @PutMapping("/doctors/{doctorId}")
    public String updateDoctor(@PathVariable int doctorId, @RequestParam String name, @RequestParam String specialty, @RequestParam String contact) {
        try {
            adminService.updateDoctor(doctorId, name, specialty, contact);
            return "의료진 정보가 수정되었습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // 의료진 삭제
    @DeleteMapping("/doctors/{doctorId}")
    public String deleteDoctor(@PathVariable int doctorId) {
        adminService.deleteDoctor(doctorId);
        return "의료진 정보가 삭제되었습니다.";
    }

    // 의료진 휴진 정보 등록
    @PostMapping("/vacations")
    public String registerVacation(@RequestParam int doctorId, @RequestParam String startDate, @RequestParam String endDate) {
        try {
            adminService.registerVacation(doctorId, startDate, endDate);
            return "의료진 휴진 정보가 등록되었습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // 의료진 휴진 정보 수정
    @PutMapping("/doctors/{doctorId}/vacation")
    public String updateDoctorVacation(
            @PathVariable int doctorId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            adminService.updateVacation(doctorId, startDate, endDate);
            return "의료진 휴진 정보가 수정되었습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (DateTimeParseException e) {
            return "날짜 형식이 잘못되었습니다. (yyyy-MM-dd)";
        }
    }

    // 의료진 휴진 정보 삭제
    @DeleteMapping("/vacations/{vacationId}")
    public String deleteVacation(@PathVariable int vacationId) {
        adminService.deleteVacation(vacationId);
        return "휴진 정보가 삭제되었습니다.";
    }

    // 건의사항 상태 변경 (읽음, 처리 중, 완료)
    @PutMapping("/inquiries/{inquiryId}/status")
    public String updateInquiryStatus(@PathVariable int inquiryId, @RequestParam String status) {
        try {
            adminService.updateInquiryStatus(inquiryId, status);
            return "건의사항 상태가 변경되었습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // 공지사항 등록
    @PostMapping("/notices")
    public String registerNotice(@RequestParam String title, @RequestParam String content) {
        try {
            adminService.registerNotice(title, content);
            return "공지사항이 등록되었습니다.";
        } catch (IllegalArgumentException e) {
            return "등록 실패: " + e.getMessage();
        }
    }

    // 공지사항 수정
    @PutMapping("/notices/{noticeId}")
    public String updateNotice(@PathVariable int noticeId, @RequestParam String title, @RequestParam String content) {
        try {
            adminService.updateNotice(noticeId, title, content);
            return "공지사항이 수정되었습니다.";
        } catch (IllegalArgumentException e) {
            return "수정 실패: " + e.getMessage();
        }
    }

    // 공지사항 삭제
    @DeleteMapping("/notices/{noticeId}")
    public String deleteNotice(@PathVariable int noticeId) {
        adminService.deleteNotice(noticeId);
        return "공지사항이 삭제되었습니다.";
    }

    // 매거진 등록
    @PostMapping("/magazines")
    public String registerMagazine(@RequestParam String title, @RequestParam String content, @RequestParam String password) {
        try {
            validatePassword(password);
            adminService.registerMagazine(title, content);
            return "건강 매거진이 등록되었습니다.";
        } catch (IllegalArgumentException e) {
            return "등록 실패: " + e.getMessage();
        }
    }

    // 매거진 수정
    @PutMapping("/magazines/{magazineId}")
    public String updateMagazine(@PathVariable int magazineId, @RequestParam String title, @RequestParam String content) {
        try {
            adminService.updateMagazine(magazineId, title, content);
            return "건강 매거진이 수정되었습니다.";
        } catch (IllegalArgumentException e) {
            return "수정 실패: " + e.getMessage();
        }
    }

    // 매거진 삭제
    @DeleteMapping("/magazines/{magazineId}")
    public String deleteMagazine(@PathVariable int magazineId) {
        adminService.deleteMagazine(magazineId);
        return "건강 매거진이 삭제되었습니다.";
    }





    // 특정 회원의 건강검진 예약 조회
    @GetMapping("/reserves/member/{memberId}")
    public List<HealthcareReserveDTO> getHealthReservesByMemberId(@PathVariable String memberId) {
        return adminService.getHealthReservesByMemberId(memberId);
    }

    // 모든 건강검진 예약 조회
    @GetMapping("/reserves")
    public List<HealthcareReserveDTO> getAllHealthReserves() {
        return adminService.getAllReservations();
    }

    // 건강검진 예약 취소
    @DeleteMapping("/reserves/{id}")
    public void cancelHealthCheck(@PathVariable Long id) {
        adminService.cancelHealthCheck(id);
    }

    // 모든 리뷰 조회
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // 특정 리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReviewById(id);
            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("리뷰 삭제 중 오류가 발생했습니다.");
        }
    }

    // 전체 리뷰 통계 조회 (전체적인 별점 평균, 세분화된 별점 평균, 총 리뷰 수)
    @GetMapping("/reviews/statistics")
    public ResponseEntity<?> getReviewStatistics() {
        try {
            var statistics = reviewService.calculateReviewStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("통계 조회 중 오류가 발생했습니다.");
        }
    }


    // 비밀번호 유효성 검사
    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해야 합니다.");
        }
    }
}