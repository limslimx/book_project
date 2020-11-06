package com.studyproject.saying;

import com.studyproject.account.AccountRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Saying;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
@Service
public class SayingService {

    private final SayingRepository sayingRepository;

    //오늘의 월과 일을 합해서 그에 해당하는 아이디를 가진 글귀객체 가져오는 메서드
    public Saying findSayingByLocalDateTime(Account account) {
        long accountId = account.getId();
        if (accountId > 100) {
            accountId -= 100;
        }
        long date = Long.parseLong(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd")));
        long month = Long.parseLong(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM")));
        long randomNum = accountId + date + month;
        Saying saying = sayingRepository.findById(randomNum).get();

        return saying;
    }

    //오늘의 글귀 강제 크롤링
    public void getTodayBookInfoFromCrawling() throws IOException {
        int j = 1;
        for (int i = 1; i < 11; i++) {
            String url = "http://www.kyobobook.co.kr/killingpart/mainCategory.laf?menugb=popular&targetPage="+i;
            Document doc = Jsoup.connect(url).get();
            Elements wisesayingElements = doc.select("div#killingPart_contents ul.list_book_info");

            Iterator<Element> wisesavingIterator = wisesayingElements.select("li").iterator();
            while (wisesavingIterator.hasNext()) {
                Element wisesayingElement = wisesavingIterator.next();
                Elements bookInfoElements = wisesayingElement.select("div.thumb_cont div.info_area");
                String title = bookInfoElements.select("div.title strong").text();
                String author = bookInfoElements.select("div.author").text().substring(0, bookInfoElements.select("div.author").text().indexOf("|"));
                String score = bookInfoElements.select("div.score_review strong").text();
                String detailCategory = bookInfoElements.select("div.book_category").text().substring(1, bookInfoElements.select("div.book_category").text().length()-1);
                String img = bookInfoElements.select("div.cover img").attr("src");

                Elements sayingElements = wisesayingElement.select("div.area_kp div.card_news");
                String sayingContent = sayingElements.select("p.part").text();
                String sayingPage = sayingElements.select("p.page_kp").text();
                String recommend = sayingElements.select("div.option span").text();

                Saying saying = Saying.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .bookTitle(title)
                        .bookAuthor(author)
                        .bookScore(score)
                        .bookCategory(detailCategory)
                        .bookImg(img)
                        .saying(sayingContent)
                        .sayingPage(sayingPage)
                        .recommend(recommend)
                        .build();

                sayingRepository.save(saying);

                log.info(j + "번째 크롤링");
                log.info("title: " + title);
                log.info("author: " + author);
                log.info("score: " + score);
                log.info("detailCategory: " + detailCategory);
                log.info("img: " + img);
                log.info("sayingContent: " + sayingContent);
                log.info("sayingPage: " + sayingPage);
                log.info("recommend: " + recommend);
                log.info("------------------------------");
                j++;
            }
        }
    }
}
