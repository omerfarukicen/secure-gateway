package org.example.handler;
import org.example.util.CookieHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        final String keycloakUserId = oAuth2User.getAttributes().get("sub").toString();
        String username = authentication.getName();
        log.info(" User : " + username + " login oldu. UserID: " + keycloakUserId);
        CookieHelper.addUserLoginCookie(exchange.getResponse());
        return new WebSessionServerRequestCache().getRedirectUri(exchange)
                .defaultIfEmpty(URI.create("/"))
                .then(new DefaultServerRedirectStrategy().sendRedirect(exchange, URI.create("http://localhost:3000/")));
    }
}
