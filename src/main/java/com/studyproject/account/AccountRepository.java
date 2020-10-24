package com.studyproject.account;

import com.studyproject.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("select a from Account a where a.email=:email")
    Account findAccountByEmail(String email);

    Account findAccountById(long id);

    Account findByNickname(String nickname);

    Optional<Account> findByEmail(String email);
}
