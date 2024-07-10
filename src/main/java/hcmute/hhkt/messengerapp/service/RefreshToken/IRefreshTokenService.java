package hcmute.hhkt.messengerapp.service.RefreshToken;

import hcmute.hhkt.messengerapp.Exception.TokenExpiredException;
import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.RefreshToken;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.Device;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(User user, Device device);
    RefreshToken recycleRefreshToken(String token, Device device) throws TokenExpiredException;
    RefreshToken invalidateToken(String token) throws TokenExpiredException;
}
