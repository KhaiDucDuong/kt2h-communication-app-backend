package vn.khaiduong.comiclibrary.service.RefreshToken;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.khaiduong.comiclibrary.Exception.TokenExpiredException;
import vn.khaiduong.comiclibrary.constant.ExceptionMessage;
import vn.khaiduong.comiclibrary.domain.RefreshToken;
import vn.khaiduong.comiclibrary.domain.User;
import vn.khaiduong.comiclibrary.repository.RefreshTokenRepository;
import vn.khaiduong.comiclibrary.util.SecurityUtil;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private final SecurityUtil securityUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public RefreshToken createRefreshToken(User user, boolean isMobile) {
        RefreshToken existingRefreshToken = refreshTokenRepository.findRefreshTokenByUserAndIsMobile(user, isMobile);
        String refreshTokenValue = securityUtil.createRefreshToken(user.getEmail());

        RefreshToken newRefreshToken;
        if(existingRefreshToken != null) {
            newRefreshToken = existingRefreshToken;
            newRefreshToken.setToken(refreshTokenValue);
            newRefreshToken.setExpiryDate(Instant.now().plusSeconds(securityUtil.getRefreshTokenExpiration()));
        } else {
            newRefreshToken = RefreshToken.builder()
                    .token(refreshTokenValue)
                    .isMobile(isMobile) // the default is false for now
                    .expiryDate(Instant.now().plusSeconds(securityUtil.getRefreshTokenExpiration()))
                    .user(user)
                    .build();
        }

        return refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public RefreshToken recycleRefreshToken(String email, String token) throws TokenExpiredException {
        RefreshToken existingRefreshToken = refreshTokenRepository.findRefreshTokenByToken(token);

        if(existingRefreshToken == null){
            throw new IllegalArgumentException(ExceptionMessage.REFRESH_TOKEN_NOT_EXIST);
        }

        if(existingRefreshToken.getExpiryDate().isBefore(Instant.now())){
            throw new TokenExpiredException(ExceptionMessage.EXPIRED_TOKEN);
        }

        String refreshTokenValue = securityUtil.createRefreshToken(email);
        existingRefreshToken.setToken(refreshTokenValue);
        existingRefreshToken.setExpiryDate(Instant.now().plusSeconds(securityUtil.getRefreshTokenExpiration()));

        return refreshTokenRepository.save(existingRefreshToken);
    }

    @Override
    public void invalidateToken(String token) throws TokenExpiredException {
        RefreshToken existingRefreshToken = refreshTokenRepository.findRefreshTokenByToken(token);

        if(existingRefreshToken == null){
            throw new IllegalArgumentException(ExceptionMessage.REFRESH_TOKEN_NOT_EXIST);
        }

        if(existingRefreshToken.getExpiryDate().isBefore(Instant.now())){
            throw new TokenExpiredException(ExceptionMessage.EXPIRED_TOKEN);
        }

        existingRefreshToken.setExpiryDate(Instant.now());
        refreshTokenRepository.save(existingRefreshToken);
    }
}
