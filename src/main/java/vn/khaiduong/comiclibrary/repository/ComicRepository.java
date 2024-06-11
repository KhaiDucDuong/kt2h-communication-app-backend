package vn.khaiduong.comiclibrary.repository;

import vn.khaiduong.comiclibrary.domain.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
}
