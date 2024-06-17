package vn.khaiduong.comiclibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.khaiduong.comiclibrary.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
