package com.studyproject.saying;

import com.studyproject.account.AccountRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Saying;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
}
