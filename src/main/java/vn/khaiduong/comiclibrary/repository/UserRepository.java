package vn.khaiduong.comiclibrary.repository;

import vn.khaiduong.comiclibrary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    Boolean existsUserByEmail(String email);
}
