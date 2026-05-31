package lt.esdc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

public class AuthenticationEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationEventListener.class);

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        log.info("Успешный вход в систему. Алхимик: {}", username);
    }

    @EventListener
    public void onFailure(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        log.info("Неудачная попытка входа! Алхимик: {}", username);
    }
}
