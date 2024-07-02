package hcmute.hhkt.messengerapp.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import hcmute.hhkt.messengerapp.constant.Constants;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtil.getCurrentUserLogin().orElse(Constants.SYSTEM));
    }
}
