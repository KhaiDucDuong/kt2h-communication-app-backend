package vn.khaiduong.comiclibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khaiduong.comiclibrary.domain.RefreshToken;
import vn.khaiduong.comiclibrary.domain.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findRefreshTokenByUserAndIsMobile(User user, boolean isMobile);
    RefreshToken findRefreshTokenByToken(String token);
}
