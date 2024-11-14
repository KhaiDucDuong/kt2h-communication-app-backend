package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.enums.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import hcmute.hhkt.messengerapp.domain.RefreshToken;
import hcmute.hhkt.messengerapp.domain.User;

import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
//    RefreshToken findRefreshTokenByUserAndIsMobile(User user, boolean isMobile);
    RefreshToken findRefreshTokenByAccountAndDevice(Account account, Device device);
    RefreshToken findRefreshTokenByToken(String token);
    RefreshToken findRefreshTokenByTokenAndDevice(String token, Device device);
}
