package com.portal.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

/**
 * Beans Nimbus (JOSE/JWT) usados pelo resource server do Spring Security.
 * O encoder assina com a chave privada RSA; o decoder valida assinatura,
 * expiracao e issuer com a chave publica.
 */
@Configuration
public class JwtConfig {

    @Bean
    JwtEncoder jwtEncoder(RsaKeyConfigProperties rsaKeys) {
        return NimbusJwtEncoder.withKeyPair(rsaKeys.publicKey(), rsaKeys.privateKey()).build();
    }

    @Bean
    JwtDecoder jwtDecoder(RsaKeyConfigProperties rsaKeys, JwtProperties jwtProperties) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(jwtProperties.issuer()));
        return decoder;
    }
}
