package com.mallang.backend.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private Long id; //공지사항 ID
    private String title; // 제목
    private String noticeWriter; // 작성자 이름
    private String password; // 비밀번호 (작성 시 필요)
    private String content; // 본문
    private String regDate; // 작성 날짜 (문자열)
}