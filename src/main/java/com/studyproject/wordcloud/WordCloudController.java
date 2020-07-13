package com.studyproject.wordcloud;

import com.studyproject.account.CurrentUser;
import com.studyproject.domain.Account;
import com.studyproject.domain.WordCloud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class WordCloudController {

    private final WordCloudService wordCloudService;
    private final WordCloudRepository wordCloudRepository;

    @GetMapping("/wordcloud/save")
    public void wordCount(@CurrentUser Account account) {
        wordCloudService.wordCount(account);
    }

    @GetMapping("/wordcloud")
    public String wordCount(@CurrentUser Account account, Model model) {
        List<WordCloud> wordCloud = wordCloudRepository.findWordCountByAccountAndCreatedDate(account, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        model.addAttribute("wordCloud", wordCloud);
        return "wordcloud/main";
    }
}
