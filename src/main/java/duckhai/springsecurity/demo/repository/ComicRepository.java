package duckhai.springsecurity.demo.repository;

import duckhai.springsecurity.demo.domain.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
}
