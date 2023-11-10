package org.example.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import org.example.util.CookieHelper;
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements ServerLogoutHandler {


    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        if (authentication.isAuthenticated() && !authentication.getPrincipal().toString().equals("anonymous")) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            final String username = authentication.getName();
            final String keycloakUserId = oAuth2User.getAttributes().get("sub").toString();
            final String sessionId = exchange.getExchange().getRequest().getCookies().getFirst("SESSION").getValue();

            try {
                SecurityContextServerLogoutHandler securityContextServerLogoutHandler = new SecurityContextServerLogoutHandler();
                securityContextServerLogoutHandler.logout(exchange, authentication);
                exchange.getExchange().getSession().flatMap(WebSession::invalidate).subscribe();
                log.info("Username: " + username + "  sessionId: " + sessionId + " is logout");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        CookieHelper.deleteCookies(exchange.getExchange());
        return Mono.empty();
    }
}
