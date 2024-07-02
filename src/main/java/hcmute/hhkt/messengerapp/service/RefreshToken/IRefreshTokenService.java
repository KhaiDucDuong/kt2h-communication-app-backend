package hcmute.hhkt.messengerapp.service.RefreshToken;

import hcmute.hhkt.messengerapp.Exception.TokenExpiredException;
import hcmute.hhkt.messengerapp.domain.RefreshToken;
import hcmute.hhkt.messengerapp.domain.User;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(User user, boolean isMobile);
    RefreshToken recycleRefreshToken(String token) throws TokenExpiredException;
    RefreshToken invalidateToken(String token) throws TokenExpiredException;
}
