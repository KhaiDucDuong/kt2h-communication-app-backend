package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findAccountByUsername(String username);
    boolean existsAccountByUsername(String username);
    Optional<Account> findAccountByActivationKey(String activationKey);
    List<Account> findAllByStatusAndCreatedDateBefore(AccountStatus status, Instant createdDate);
}
