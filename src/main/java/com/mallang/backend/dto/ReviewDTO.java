package com.mallang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;                 // 리뷰 ID
    private Long memberId;           // 작성자 ID
    private Long doctorId;           // 의사 ID
    private Long departmentId;       // 부서 ID
    private List<Integer> detailStars; // 세부 별점 리스트
    private String content;          // 리뷰 본문
    private String memberPassword;   // 작성자 비밀번호
    private String file;      // 첨부 파일
}