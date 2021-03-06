package com.studyproject.scheduler;

import com.studyproject.book.BookRepository;
import com.studyproject.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BookCrawlingScheduler {

    private final BookRepository bookRepository;

    //소설 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void literature_novelCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "소설", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "문학";
            String detailCategory = "소설";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=B&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //에세이 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void literature_essayCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "시에세이", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "문학";
            String detailCategory = "시에세이";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=C&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //시 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void literature_poemCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "시에세이", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "문학";
            String detailCategory = "시에세이";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=F&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //인문 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void humanities_humanitiesCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "인문", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "인문";
            String detailCategory = "인문";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=I&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //정치사회 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void humanities_societyCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "정치사회", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "인문";
            String detailCategory = "정치사회";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=J&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //경제경영 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void humanities_economyCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "경제경영", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "인문";
            String detailCategory = "경제경영";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=K&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //역사문화 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void humanities_historyCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "역사문화", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "인문";
            String detailCategory = "역사문화";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=b&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //교양과학 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void real_scienceCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "교양과학", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "실용";
            String detailCategory = "교양과학";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=M&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //외국어 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void real_languageCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "외국어", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "실용";
            String detailCategory = "외국어";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=N&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //예술 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void real_artCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "예술", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "실용";
            String detailCategory = "예술";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=Q&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //여행 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void real_tripCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "여행", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "실용";
            String detailCategory = "여행";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=d&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //자기계발 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void selfDevelopment_selfDevelopmentCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "자기계발", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "자기계발";
            String detailCategory = "자기계발";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=c&range=1&kind=0&orderClick=DAb";
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("ul.list_type01");

            Iterator<Element> bookIterator = elements.select(" > li").iterator();

            int i = 1;
            while (bookIterator.hasNext()) {
                Element bookInfo = bookIterator.next();
                String rank = bookInfo.select("div.cover a strong.rank").text();
                String img = bookInfo.select("div.cover a img").attr("src");
                String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
                String subTitle = bookInfo.select("div.detail div.subtitle").text();

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(null)
                        .name(title)
                        .subName(subTitle)
                        .category(categoryValue)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(bookUrl)
                        .rank(rank)
                        .tag(tag)
                        .bestCellar(true)
                        .publicationDate(publicationDate)
                        .build();
                bookRepository.save(book);
                bookList.add(book);

                log.info("--------------------");
                log.info(i + "번째 도서 크롤링 시작");
                log.info("rank: " + rank);
                log.info("name: " + title);
                log.info("subName: " + subTitle);
                log.info("category: " + detailCategory);
                log.info("img: " + img);
                log.info("author: " + author);
                log.info("url: " + bookUrl);
                log.info("tag: " + tag);
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //소설 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategoryNovelCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "소설", false);

        String detailCategory = "소설";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=01&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=01&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //시&에세이 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategoryPoemAndEssayCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "시에세이", false);

        String detailCategory = "시에세이";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=03&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=03&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //인문 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategoryHumanityCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "인문", false);

        String detailCategory = "인문";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=05&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=05&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //정치사회 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategorySocietyCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "정치사회", false);

        String detailCategory = "정치사회";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=17&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=17&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //경제경영 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategoryEconomyCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "경제경영", false);

        String detailCategory = "경제경영";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=13&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=13&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //역사문화 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategoryHistoryCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "역사문화", false);

        String detailCategory = "역사문화";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=19&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=19&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //교양과학 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategoryScienceCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "교양과학", false);

        String detailCategory = "교양과학";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=29&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=29&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //외국어 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategoryLanguageCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "외국어", false);

        String detailCategory = "외국어";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=27&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=27&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //여행 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategoryTravelCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "여행", false);

        String detailCategory = "여행";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=32&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=32&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //예술 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategoryArtCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "예술", false);

        String detailCategory = "예술";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=23&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=23&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }

    //자기계발 카테고리 크롤링 메서드
    @Scheduled(cron = "0 20 15 * * *")
    public void bookCategorySelfDevelopmentCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "자기계발", false);

        String detailCategory = "자기계발";
        List<Book> bookList = new ArrayList<Book>();

        if (count == 0) {
            int j = 1;
            for (int i = 1; i < 9; i++) {
                String listUrl = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=15&menuCode=002&targetPage=" + i;
                Document doc = Jsoup.connect(listUrl).get();
                Elements totalElements = doc.select("div#wrapper div#wrap div#container div.content_middle div.showcase_template div.prd_list_area ul.prd_list_type1");
                Iterator<Element> bookIterator = totalElements.select(" > li.id_detailli").iterator();

                while (bookIterator.hasNext()) {
                    Element bookInfo = bookIterator.next();
                    String img = bookInfo.select("div.cover img").attr("src");
                    String rank = bookInfo.select("div.cover a span em.best_flag span").text();
                    String author = bookInfo.select("div.pub_info span.author").text();
                    String publicationDate = bookInfo.select("div.pub_info span.publication:nth-child(3)").text();
                    int location = img.lastIndexOf("/");
                    int location2 = img.lastIndexOf(".");
                    String barcode = img.substring(location + 2, location2);

                    String detailUrl = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&linkClass=15&barcode=" + barcode;
                    Document doc2 = Jsoup.connect(detailUrl).get();
                    String title = doc2.select("div.box_detail_point h1.title > strong").text();
                    String subTitle = doc2.select("div.box_detail_point h1.title span.back strong").text();
                    String tag = doc2.select("div.tag_list a span em").text();
                    String[] splitTag = tag.split(" ");
                    if (splitTag.length > 3) {
                        tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                    }

                    Book book = Book.builder()
                            .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .searchBy(null)
                            .name(title)
                            .subName(subTitle)
                            .category(null)
                            .detailCategory(detailCategory)
                            .img(img)
                            .author(author)
                            .url(detailUrl)
                            .rank(rank)
                            .tag(tag)
                            .bestCellar(false)
                            .publicationDate(publicationDate)
                            .build();
                    bookRepository.save(book);

                    log.info("--------------------");
                    log.info(j + "번째 도서 크롤링 시작");
                    log.info("rank: " + rank);
                    log.info("name: " + title);
                    log.info("subName: " + subTitle);
                    log.info("category: " + detailCategory);
                    log.info("img: " + img);
                    log.info("author: " + author);
                    log.info("url: " + detailUrl);
                    log.info("tag: " + tag);
                    log.info("publicationDate: " + publicationDate);
                    log.info("--------------------");
                    j++;
                }
            }
            log.info("---------------crawling end---------------");
        }
    }
}

