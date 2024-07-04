package hcmute.hhkt.messengerapp.service.RefreshToken;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.enums.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hcmute.hhkt.messengerapp.Exception.TokenExpiredException;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.RefreshToken;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.repository.RefreshTokenRepository;
import hcmute.hhkt.messengerapp.util.SecurityUtil;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {
    private final SecurityUtil securityUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public RefreshToken createRefreshToken(User user, boolean isMobile) {
//        RefreshToken existingRefreshToken = refreshTokenRepository.findRefreshTokenByUserAndIsMobile(user, isMobile);
        RefreshToken existingRefreshToken = refreshTokenRepository.findRefreshTokenByAccountAndDevice(new Account(), Device.BROWSER);
        String refreshTokenValue = securityUtil.createRefreshToken(user.getEmail());

        RefreshToken newRefreshToken;
        if(existingRefreshToken != null) {
            newRefreshToken = existingRefreshToken;
            newRefreshToken.setToken(refreshTokenValue);
            newRefreshToken.setExpiryDate(Instant.now().plusSeconds(securityUtil.getRefreshTokenExpiration()));
        } else {
            newRefreshToken = RefreshToken.builder()
                    .token(refreshTokenValue)
                    //.isMobile(isMobile) // the default is false for now
                    .device(Device.BROWSER) //the default is BROWSER for now
                    .expiryDate(Instant.now().plusSeconds(securityUtil.getRefreshTokenExpiration()))
                    //.user(user)
                    .account(new Account())
                    .build();
        }

        return refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public RefreshToken recycleRefreshToken(String token) throws TokenExpiredException {
        RefreshToken existingRefreshToken = refreshTokenRepository.findRefreshTokenByToken(token);

        if(existingRefreshToken == null){
            throw new IllegalArgumentException(ExceptionMessage.REFRESH_TOKEN_NOT_EXIST);
        }

        if(existingRefreshToken.getExpiryDate().isBefore(Instant.now())){
            throw new TokenExpiredException(ExceptionMessage.EXPIRED_TOKEN);
        }

//        String refreshTokenValue = securityUtil.createRefreshToken(existingRefreshToken.getUser().getEmail());
        String refreshTokenValue = securityUtil.createRefreshToken(existingRefreshToken.getAccount().getUsername());
        existingRefreshToken.setToken(refreshTokenValue);
        existingRefreshToken.setExpiryDate(Instant.now().plusSeconds(securityUtil.getRefreshTokenExpiration()));

        return refreshTokenRepository.save(existingRefreshToken);
    }

    @Override
    public RefreshToken invalidateToken(String token) throws TokenExpiredException {
        RefreshToken existingRefreshToken = refreshTokenRepository.findRefreshTokenByToken(token);

        if(existingRefreshToken == null){
            throw new IllegalArgumentException(ExceptionMessage.REFRESH_TOKEN_NOT_EXIST);
        }

        if(existingRefreshToken.getExpiryDate().isBefore(Instant.now())){
            throw new TokenExpiredException(ExceptionMessage.EXPIRED_TOKEN);
        }

        existingRefreshToken.setExpiryDate(Instant.now());
        return refreshTokenRepository.save(existingRefreshToken);
    }
}
