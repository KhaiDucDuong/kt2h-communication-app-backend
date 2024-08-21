package hcmute.hhkt.messengerapp.service.MailService;

import hcmute.hhkt.messengerapp.domain.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MailService {
    private final Logger log = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageSource messageSource;

    @Value("${spring.mail.username}")
    private String hostEmail;
    @Value("${front-end.website.url}")
    private String websiteRootUrl;
    private static final String USER = "user";
    private static final String ACTIVATION_CODE = "activationCode";
    private static final String RESET_CODE = "reset";
    private static final String WEBSITE_ROOT_URL = "webUrl";

    private void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
                "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart,
                isHtml,
                to,
                subject,
                content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(hostEmail);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    private void sendEmailFromTemplateSync(User user, Context context, Locale locale, String templateName, String titleKey) {
        if(user.getEmail() == null){
            log.debug("Email doesn't exist for user '{}'", user.getId());
            return;
        }

        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        this.sendEmailSync(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user, String activationCode) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        Locale locale = Locale.ENGLISH;
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(ACTIVATION_CODE, activationCode);
        context.setVariable(WEBSITE_ROOT_URL, websiteRootUrl);
        this.sendEmailFromTemplateSync(user, context, locale, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
//        this.sendEmailFromTemplateSync(user, "mail/creationEmail", "email.creation.title");
    }
}
