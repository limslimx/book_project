package com.studyproject.saying;

import com.studyproject.account.CurrentUser;
import com.studyproject.domain.Account;
import com.studyproject.domain.Saying;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class SayingController {

    private final SayingService sayingService;

    @GetMapping("/saying/today")
    public String todaySaying(@CurrentUser Account account, Model model) {
        Saying saying = sayingService.findSayingByLocalDateTime(account);
        model.addAttribute("account", account);
        model.addAttribute("saying", saying);
        return "saying/today-saying";
    }

    @GetMapping("/saying/today/save")
    @ResponseBody
    public String todaySayingSave() throws IOException {
        sayingService.getTodayBookInfoFromCrawling();
        return "saying crawling success";
    }
}
