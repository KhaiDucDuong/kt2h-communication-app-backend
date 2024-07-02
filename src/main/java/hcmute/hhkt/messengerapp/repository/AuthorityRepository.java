package hcmute.hhkt.messengerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hcmute.hhkt.messengerapp.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
