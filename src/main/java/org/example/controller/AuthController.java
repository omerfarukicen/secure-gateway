package org.example.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @GetMapping("/user-info")
    public Mono<Map<String, Object>> getUserInfo(Authentication auth) {
        if (auth.getPrincipal() instanceof OidcUser user) {
            return Mono.just(
                    Objects.requireNonNull(user.getAttributes()));
        } else {
            return Mono.empty();
        }
    }

}
