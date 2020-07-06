package com.studyproject.bookReview;

import com.studyproject.account.AccountRepository;
import com.studyproject.account.CurrentUser;
import com.studyproject.book.BookRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.domain.BookReview;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BookReviewService {

    private final AccountRepository accountRepository;
    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;
    private final ModelMapper modelMapper;

    //독서록 저장 메서드
    public void createBookReview(Account account, Long id, BookReviewForm bookReviewForm) {
        Book bookById = bookRepository.findBookById(id);
        BookReview bookReview = BookReview.builder()
                .title(bookReviewForm.getTitle())
                .content(bookReviewForm.getContent())
                .createDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .modifiedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .isOpen(false)
                .account(account)
                .book(bookById)
                .build();

        bookReviewRepository.save(bookReview);
    }

    //독서록 조회 메서드
    public BookReview getBookReviewToUpdate(Account account, Long bookReviewId) {
        BookReview bookReview = this.getBookReview(bookReviewId);
        if (!account.isManagerOfBookReview(bookReview)) {
            throw new AccessDeniedException("독서록 수정 기능을 사용할 수 없습니다.");
        }
        return bookReview;
    }

    //독서록 조회 메서드
    public BookReview getBookReview(Long bookReviewId) {
        BookReview bookReview = bookReviewRepository.findBookReviewById(bookReviewId);
        if (bookReview == null) {
            throw new IllegalArgumentException(bookReviewId + "에 해당하는 독서록이 존재하지 않습니다.");
        }
        return bookReview;
    }

    //독서록 수정 메서드
    public void updateBookReview(BookReview bookReview, BookReviewForm bookReviewForm) {
        modelMapper.map(bookReviewForm, bookReview);
        bookReview.setModifiedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    //독서록 삭제 메서드
    public void deleteBookReview(Long bookReviewId, Account account) {
        bookReviewValidation(bookReviewId, account);
        BookReview bookReview = bookReviewRepository.findBookReviewById(bookReviewId);
        bookReviewRepository.delete(bookReview);
    }

    //독서록 유효성 검사
    public void bookReviewValidation(Long bookReviewId, Account account) {
        BookReview bookReviewById = bookReviewRepository.findBookReviewById(bookReviewId);
        if (bookReviewById == null) {
            throw new IllegalArgumentException("해당하는 독서록이 없습니다.");
        }
        if (!bookReviewById.getAccount().equals(account)) {
            throw new IllegalArgumentException("독서록 삭제 권한이 없습니다.");
        }
    }

    public void bookReviewOpen(Long bookReviewId) {
        BookReview bookReview = bookReviewRepository.findBookReviewById(bookReviewId);
        bookReview.setOpen(true);
    }

    public void bookReviewClose(Long bookReviewId) {
        BookReview bookReview = bookReviewRepository.findBookReviewById(bookReviewId);
        bookReview.setOpen(false);
    }
//
//    public void insertData(Account account) {
//
//        for (int i = 0; i < 30; i++) {
//            Book book = bookRepository.findBookById(33387L);
//            BookReview bookReview = BookReview.builder()
//                    .book(book)
//                    .title("독서록 작성 테스트")
//                    .content("독서록 본문입니다.")
//                    .account(account)
//                    .isOpen(true)
//                    .createDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
//                    .modifiedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
//                    .build();
//
//            bookReviewRepository.save(bookReview);
//        }
//    }

}
