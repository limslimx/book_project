package com.studyproject.wordcloud;

import com.studyproject.domain.Account;
import com.studyproject.domain.WordCloud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordCloudRepository extends JpaRepository<WordCloud, Long> {

    List<WordCloud> findWordCountByAccountAndCreatedDate(Account account, String yyyyMMdd);
}
