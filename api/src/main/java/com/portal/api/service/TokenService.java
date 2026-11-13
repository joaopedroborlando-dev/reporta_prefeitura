package com.portal.api.service;

import com.portal.api.auth.UserAuth;
import com.portal.api.config.JwtProperties;
import com.portal.api.dto.TokenResponse;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public TokenService(JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    public TokenResponse generateToken(UserAuth userAuth) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.expiration());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(userAuth.getUsername())
                .claim("uid", userAuth.getUser().getId())
                .claim("email", userAuth.getUser().getEmail())
                .claim("roles", userAuth.getRoles())
                .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(header, claims));

        return new TokenResponse(jwt.getTokenValue(), "Bearer", jwtProperties.expiration().toSeconds());
    }
}
