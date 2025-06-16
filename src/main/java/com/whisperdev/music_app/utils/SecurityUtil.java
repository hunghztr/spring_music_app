package com.whisperdev.music_app.utils;

import com.whisperdev.music_app.dto.user.UserToken;
import com.whisperdev.music_app.model.User;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class SecurityUtil {

    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${whisper.jwt.base64-secret}")
    private String jwtKey;

    @Value("${whisper.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${whisper.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;


    public String generateToken(User user, String type) {
        UserToken userToken = new UserToken();
        userToken.setId(user.getId());
        userToken.setUsername(user.getUsername());
        userToken.setName(user.getName());
        userToken.setRole(user.getRole());
        long expiration = 0;
        if (type.equals("access_token")) {
            expiration = accessTokenExpiration;
        } else {
            expiration = refreshTokenExpiration;
        }

        Instant now = Instant.now();
        Instant expir = now.plus(expiration, ChronoUnit.SECONDS);
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        JwtClaimsSet claimsSet = buildClaimset(expir, now, userToken, type);
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();

    }

    private JwtClaimsSet buildClaimset(Instant expir, Instant now, UserToken userToken, String type) {

        if (type.equals("access_token")) {
            return JwtClaimsSet.builder()
                    .issuedAt(now)
                    .expiresAt(expir)
                    .subject(userToken.getUsername())
                    .claim("user", userToken)
                    .claim("permission", userToken.getRole())
                    .build();
        } else {
            return JwtClaimsSet.builder()
                    .issuedAt(now)
                    .expiresAt(expir)
                    .subject(userToken.getUsername())
                    .claim("user", userToken)
                    .build();
        }


    }

}
