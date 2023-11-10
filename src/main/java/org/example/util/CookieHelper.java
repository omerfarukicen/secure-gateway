package org.example.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.util.Set;
import java.util.UUID;

@UtilityClass
public class CookieHelper {
    private static final String IS_USER_LOGIN_COOKIE_NAME = "XSRF-TOKEN";

    public static HttpCookie getUserLoginCookie(ServerHttpRequest request) {
        return request.getCookies().getFirst(IS_USER_LOGIN_COOKIE_NAME);
    }

    public static void addUserLoginCookie(ServerHttpResponse response) {
        ResponseCookie tokenCookie = ResponseCookie
                .from(IS_USER_LOGIN_COOKIE_NAME, UUID.randomUUID().toString())
                .httpOnly(true)
//                .secure(true)
                .path("/").build();
        response.addCookie(tokenCookie);
    }


    public static void deleteCookies(ServerWebExchange swe) {
        Set<String> requestCookieNames = swe.getRequest().getCookies().keySet();
        requestCookieNames.stream().filter(requestCookieName -> requestCookieName.contains(IS_USER_LOGIN_COOKIE_NAME))
                .forEach(cookie -> {
                    ResponseCookie responseCookie = ResponseCookie
                            .from(cookie, "")
                            .maxAge(0)
                            .path("/").build();
                    swe.getResponse().addCookie(responseCookie);
                });
    }


}
