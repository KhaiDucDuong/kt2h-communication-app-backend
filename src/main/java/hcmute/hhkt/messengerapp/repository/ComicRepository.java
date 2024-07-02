package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
}
