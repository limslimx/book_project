package com.studyproject.domain;

import lombok.*;

import javax.persistence.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@Entity
public class WordCloud {

    @Column(name = "wordCloud_id")
    @Id @GeneratedValue
    private Long id;

    private String wordname;

    private String wordcount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String createdDate;
}
