package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findAccountByUsername(String username);
}
