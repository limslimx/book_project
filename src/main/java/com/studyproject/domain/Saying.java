package com.studyproject.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
@Entity
public class Saying {

    @Column(name = "saying_id")
    @Id @GeneratedValue
    private Long id;

    //db에서 조회시에 날짜별로 구분하기 위한 컬럼
    private String searchDate;

    //대상 도서 제목
    private String bookTitle;

    //대상 도서 저자
    private String bookAuthor;

    //대상 도서 클로버평점
    private String bookScore;

    //대상 도서 카테고리
    private String bookCategory;

    //대상 도서 이미지
    private String bookImg;

    //글귀
    private String saying;

    //글귀가 적혀있는 페이지수
    private String sayingPage;

    //글귀 추천수
    private String recommend;
}
