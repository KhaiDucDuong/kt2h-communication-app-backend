package vn.khaiduong.comiclibrary.service.RefreshToken;

import vn.khaiduong.comiclibrary.Exception.TokenExpiredException;
import vn.khaiduong.comiclibrary.domain.RefreshToken;
import vn.khaiduong.comiclibrary.domain.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user, boolean isMobile);
    RefreshToken recycleRefreshToken(String email,String token) throws TokenExpiredException;

    void invalidateToken(String token) throws TokenExpiredException;
}
