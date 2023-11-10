package org.example.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class JwtHelper {

    private static int EXPIRE_TIME = 60;

    @Value("${spring.security.oauth2.client.registration.gateway.client-secret}")
    private String secret;

    private  JWSSigner signer;
    private JWSVerifier verifier;

    @PostConstruct
    public void initJwtHelper() throws JOSEException {
        signer = new MACSigner(secret);
        verifier = new MACVerifier(secret);
    }


    public  String generateIdentifierToken(String username) throws JOSEException {
        SignedJWT jws = createJWS(username);
        jws.sign(signer);
        return jws.serialize();
    }

    private  SignedJWT createJWS(String username) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + EXPIRE_TIME * 1000 * 60);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(createdDate)
                .expirationTime(expirationDate)
                .build();

        return new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).keyID("gateway").build(), claims);
    }

    public  String getUsername(String authCookie) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(authCookie);
            if (!signedJWT.verify(verifier)) {
                throw new JOSEException("JWT Exception");
            }
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            String userID = jwtClaimsSet.getSubject();
            return userID;
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
