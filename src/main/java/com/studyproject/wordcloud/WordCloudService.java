package com.studyproject.wordcloud;

import com.studyproject.account.CurrentUser;
import com.studyproject.domain.Account;
import com.studyproject.domain.WordCloud;
import com.studyproject.favorBook.FavorBookRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class WordCloudService {

    private final FavorBookRepository favorBookRepository;
    private final WordCloudRepository wordCloudRepository;

    public void wordCount(Account account) {

        List<String> bookTagList = favorBookRepository.findBookTagByAccount(account);
//        List<String> tagList = new ArrayList<String>();
//        WordCloud wordCloud = new WordCloud();
//
//        for (int i = 0; i < bookTagList.size(); i++) {
//            if (!bookTagList.get(i).equals("")) {
//                String[] tag = bookTagList.get(i).split(" ");
//                for (int j = 0; j < tag.length; j++) {
//                    if (tagList.contains(tag[j].substring(1))) {
//
//                    } else {
//
//                    }
//                }
//            }
//        }


        // 코모란 시작
        // 분석 결과값이 나온 단어 리스트
        List<String> komoranList = new ArrayList<String>();

        // 코모란 분석기 돌리고 나온 단어 리스트 를 카운팅 한 리스트
        List<String> kWordList = new ArrayList<String>();
        List<Integer> kCntList = new ArrayList<Integer>();

        Iterator<String> InList = bookTagList.iterator();

        while (InList.hasNext()) {
            log.info("문장 분석중입니다.");
            Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
            KomoranResult analyKomoranResult = komoran.analyze(InList.next());
            komoranList.addAll(analyKomoranResult.getNouns());
            komoran = null;
            analyKomoranResult = null;
        }
        for (int i = 0; i < komoranList.size(); i++) {
            if (komoranList.get(i).length() > 1) {
                if (!kWordList.contains(komoranList.get(i))) {
                    kWordList.add(komoranList.get(i));
                    int tmp = 0;
                    for (int j = 0; j < komoranList.size(); j++) {
                        if (komoranList.get(i).equals(komoranList.get(j)))
                            tmp++;
                    }
                    kCntList.add(tmp);
                }
            }
        }

        List<WordCloud> wordCloudList = new ArrayList<WordCloud>();
        for (int i = 0; i < kWordList.size(); i++) {
            WordCloud wordCloud = new WordCloud();
            wordCloud.setWordname(kWordList.get(i));
            wordCloud.setWordcount(Integer.toString(kCntList.get(i)));
            wordCloud.setAccount(account);
            wordCloud.setCreatedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            wordCloudRepository.save(wordCloud);
            wordCloudList.add(wordCloud);
            wordCloud = null;
        }
        kWordList = null;
        kCntList = null;

        for (int i = 0; i < wordCloudList.size(); i++) {
            log.info("count: "+wordCloudList.get(i).getWordcount());
            log.info("name: "+wordCloudList.get(i).getWordname());
        }

        //코모란 끝

    }
}
