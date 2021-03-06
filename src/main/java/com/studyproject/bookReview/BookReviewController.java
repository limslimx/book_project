package com.studyproject.bookReview;

import com.studyproject.account.AccountRepository;
import com.studyproject.account.CurrentUser;
import com.studyproject.book.BookRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.domain.BookReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookReviewController {

    private final BookRepository bookRepository;
    private final BookReviewService bookReviewService;
    private final AccountRepository accountRepository;
    private final BookReviewRepository bookReviewRepository;
    private final ModelMapper modelMapper;

    //독서록 폼 핸들러
    @GetMapping("/bookReview/{id}")
    public String bookReviewSaveForm(@CurrentUser Account account, @PathVariable Long id, Model model) {
        Book bookById = bookRepository.findBookById(id);
        model.addAttribute("book", bookById);
        model.addAttribute("account", account);
        model.addAttribute("bookReviewForm", new BookReviewForm());
        return "bookReview/save-form";
    }

    //독서록 db에 저장 기능 핸들러
    @PostMapping("/bookReview/save/{id}")
    public String bookReviewSave(@CurrentUser Account account, @PathVariable Long id, @Valid BookReviewForm bookReviewForm, Errors errors, Model model) {
        Book bookById = bookRepository.findBookById(id);
        if (errors.hasErrors()) {
            log.info("##############fail");
            model.addAttribute("account", account);
            model.addAttribute("book", bookById);
            return "bookReview/save-form";
        }
        log.info("##############success");
        bookReviewService.createBookReview(account, id, bookReviewForm);
        return "redirect:/bookReview/list/" + account.getId();
    }

    //독서록 리스트 화면 핸들러
    @GetMapping("/bookReview/list/{nickname}")
    public String bookReviewList(@CurrentUser Account account, @PathVariable String nickname, Model model) {
        Account byId = accountRepository.findAccountById(account.getId());

        if (byId == null) {
            throw new IllegalArgumentException(nickname + "에 해당되는 사용자가 없습니다.");
        }
        List<BookReview> bookReviewList = bookReviewRepository.findByAccount(byId);
        model.addAttribute("bookReviewList", bookReviewList);
        model.addAttribute("nickname", nickname);
        model.addAttribute("account", account);
        return "bookReview/list";
    }

    //독서록 상세 화면 핸들러
    @GetMapping("/bookReview/detail/{bookReviewId}")
    public String bookReviewDetail(@CurrentUser Account account, @PathVariable Long bookReviewId, Model model) {
        BookReview bookReview = bookReviewService.getBookReview(bookReviewId);
        Account accountByBookReview = bookReview.getAccount();
        model.addAttribute("account", account);
        model.addAttribute("bookReview", bookReview);
        model.addAttribute("isOwner", accountByBookReview.equals(account));
        return "bookReview/detail";
    }

    //독서록 수정 화면 핸들러
    @GetMapping("/bookReview/update/{bookReviewId}")
    public String bookReviewUpdateForm(@CurrentUser Account account, @PathVariable Long bookReviewId, Model model) {
        BookReview bookReview = bookReviewService.getBookReviewToUpdate(account, bookReviewId);
        model.addAttribute("account", account);
        model.addAttribute("bookReview", bookReview);
        model.addAttribute("book", bookRepository.findBookById(bookReview.getBook().getId()));
        model.addAttribute(modelMapper.map(bookReview, BookReviewForm.class));
        return "bookReview/update-form";
    }

    //독서록 수정 기능 핸들러
    @PostMapping("/bookReview/update/{bookReviewId}")
    public String bookReviewUpdate(@CurrentUser Account account, @PathVariable Long bookReviewId, @Valid BookReviewForm bookReviewForm, Errors errors, Model model) {
        BookReview bookReview = bookReviewService.getBookReviewToUpdate(account, bookReviewId);
        if (errors.hasErrors()) {
            //TODO 독서록 수정 시에 내용에 아무것도 입력하지 않으면 오류 바인딩하도록 설정하기
            log.info("##################fail##################");
            model.addAttribute("bookReviewId", bookReviewId);
            model.addAttribute("account", account);
            return "bookReview/update-form";
        }
        bookReviewService.updateBookReview(bookReview, bookReviewForm);

        log.info("##################success##################");

        return "redirect:/bookReview/detail/" + bookReview.getId();
    }

    //독서록 삭제 기능 핸들러
    @GetMapping("/bookReview/delete/{bookReviewId}")
    public String bookReviewDelete(@CurrentUser Account account, @PathVariable Long bookReviewId, RedirectAttributes attributes) {
        bookReviewService.deleteBookReview(bookReviewId, account);
        attributes.addFlashAttribute("message", "독서록을 삭제하였습니다.");
        return "redirect:/bookReview/list/" + account.getId();
    }

    //독서록 공개로 변경 핸들러
    @PostMapping("/bookReview/open/{bookReviewId}")
    public @ResponseBody long bookReviewOpen(@CurrentUser Account account, @PathVariable Long bookReviewId) {
        bookReviewService.bookReviewOpen(bookReviewId);
        return account.getId();
    }

    //독서록 비공개로 변경 핸들러
    @PostMapping("/bookReview/close/{bookReviewId}")
    public @ResponseBody long bookReviewClose(@CurrentUser Account account, @PathVariable Long bookReviewId) {
        bookReviewService.bookReviewClose(bookReviewId);
        return account.getId();
    }
}
