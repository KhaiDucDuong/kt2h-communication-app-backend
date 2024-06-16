package vn.khaiduong.comiclibrary.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import vn.khaiduong.comiclibrary.constant.Constants;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtil.getCurrentUserLogin().orElse(Constants.SYSTEM));
    }
}
