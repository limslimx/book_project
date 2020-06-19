package com.studyproject.main;

import com.studyproject.account.CurrentUser;
import com.studyproject.bookReview.BookReviewRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.favorBook.FavorBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MainService mainService;
    private final FavorBookRepository favorBookRepository;

    @GetMapping("/")
    public String index(@CurrentUser Account account, Model model) {
        if (account != null) {
            List<Book> bookRecommendList = mainService.bookRecommend(account);
            List<String> favorBookList = favorBookRepository.findBookNameByAccountId(account.getId());
            model.addAttribute("bookRecommendList", bookRecommendList);
            model.addAttribute("favorBookList", favorBookList);
            model.addAttribute(account);
        }
        return "index";
    }

    //로그인 폼 핸들러
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
