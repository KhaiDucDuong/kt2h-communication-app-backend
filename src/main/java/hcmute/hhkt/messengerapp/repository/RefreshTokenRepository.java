package hcmute.hhkt.messengerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import hcmute.hhkt.messengerapp.domain.RefreshToken;
import hcmute.hhkt.messengerapp.domain.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findRefreshTokenByUserAndIsMobile(User user, boolean isMobile);
    RefreshToken findRefreshTokenByToken(String token);
}
