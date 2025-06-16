package com.whisperdev.music_app.utils;//package com.whisperdev.music_app.utils;
//
//
//
//
//import com.nimbusds.jose.JWSAlgorithm;
//import com.nimbusds.jose.JWSHeader;
//import com.nimbusds.jose.JWSObject;
//import com.nimbusds.jose.Payload;
//import com.nimbusds.jose.crypto.MACSigner;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.whisperdev.music_app.model.User;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.JwsHeader;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import com.whisperdev.music_app.dto.user.UserToken;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Base64;
//import java.util.Date;
//
//
//@Service
//public class SecurityUtil {
//    @Value("${whisper.jwt.base64-secret}")
//    public String key;
//    @Value("${whisper.jwt.access-token-validity-in-seconds}")
//    private long expirationAcc;
//    @Value("${whisper.jwt.refresh-token-validity-in-seconds}")
//    private long expirationRe;
//
//

import com.nimbusds.jose.util.Base64;
import com.whisperdev.music_app.dto.user.ResLoginDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/// /    public String generateAccessToken(UserToken userToken) {
/// /
/// /        var key = Base64.getDecoder().decode(KEY);
/// /        Instant now = Instant.now();
/// /        Instant expir = now.plus(expirationAcc, ChronoUnit.SECONDS);
/// /        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
/// /        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
/// /                .issueTime(new Date())
/// /                .expirationTime(new Date(expir.toEpochMilli()))
/// /                .subject(userToken.getName())
/// /                .claim("user", userToken)
/// /                .claim("permission", userToken.getRole())
/// /                .build();
/// /        Payload payload = new Payload(claimsSet.toJSONObject());
/// /        JWSObject object = new JWSObject(header, payload);
/// /        try {
/// /            object.sign(new MACSigner(key));
/// /        } catch (Exception e) {
/// /            // TODO Auto-generated catch block
/// /            e.printStackTrace();
/// /        }
/// /        return object.serialize();
/// /    }
/// /
/// /    public String generateRefreshToken(UserToken userToken) {
/// /
/// /        var key = Base64.getDecoder().decode(KEY);
/// /        Instant now = Instant.now();
/// /        Instant expir = now.plus(expirationRe, ChronoUnit.SECONDS);
/// /        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
/// /        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
/// /                .issueTime(new Date())
/// /                .expirationTime(new Date(expir.toEpochMilli()))
/// /                .subject(userToken.getName())
/// /                .claim("user", userToken)
/// /                .build();
/// /        Payload payload = new Payload(claimsSet.toJSONObject());
/// /        JWSObject object = new JWSObject(header, payload);
/// /        try {
/// /            object.sign(new MACSigner(key));
/// /        } catch (Exception e) {
/// /            // TODO Auto-generated catch block
/// /            e.printStackTrace();
/// /        }
/// /        return object.serialize();
/// /    }
//
//    public String generateToken(User user, String type) {
//        long expiration = 0;
//        if (type.equals("access_token")) {
//            expiration = expirationAcc;
//        } else {
//            expiration = expirationRe;
//        }
//        var getKey = Base64.getDecoder().decode(key);
//
//        Instant now = Instant.now();
//        Instant expir = now.plus(expiration, ChronoUnit.SECONDS);
//        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
//
//        JWTClaimsSet claimsSet = buildClaimset(expir,user, type);
//
//        Payload payload = new Payload(claimsSet.toJSONObject());
//        JWSObject object = new JWSObject(header, payload);
//        try {
//            object.sign(new MACSigner(getKey));
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return object.serialize();
//    }
//
//    private JWTClaimsSet buildClaimset(Instant expir, User user, String type) {
//        if (type.equals("access_token")) {
//            return new JWTClaimsSet.Builder()
//                    .issueTime(new Date())
//                    .expirationTime(new Date(expir.toEpochMilli()))
//                    .subject(user.getName())
//                    .claim("role", user.getRole())
//                    .build();
//        } else {
//            return new JWTClaimsSet.Builder()
//                    .issueTime(new Date())
//                    .expirationTime(new Date(expir.toEpochMilli()))
//                    .subject(user.getName())
//                    .claim("role", user.getRole())
//                    .build();
//        }
//
//
//    }
//}

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

    public String createAccessToken(String email, ResLoginDTO dto) {
        ResLoginDTO.UserInsideToken userToken = new ResLoginDTO.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setName(dto.getUser().getName());

        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        // hardcode permission (for testing)
        List<String> listAuthority = new ArrayList<String>();

        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userToken)
                .claim("permission", listAuthority)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    public String createRefreshToken(String email, ResLoginDTO dto) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        ResLoginDTO.UserInsideToken userToken = new ResLoginDTO.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setName(dto.getUser().getName());

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userToken)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                JWT_ALGORITHM.getName());
    }

    public Jwt checkValidRefreshToken(String token){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> Refresh Token error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
    }
