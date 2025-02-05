package org.example.componenteauth

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import java.security.interfaces.RSAPublicKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain
import java.security.interfaces.RSAPrivateKey

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${jwt.public.key}") val rsaPublicKey: RSAPublicKey,
    @Value("\${jwt.private.key}") val rsaPrivateKey: RSAPrivateKey
) {



    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { auth ->
            auth.requestMatchers("/product/getAll").permitAll()
                .requestMatchers("/product/findMany").permitAll()
                .anyRequest().authenticated()
        }

        http.csrf {
            it.disable()
        }

        http.headers { h ->
            h.frameOptions {
                it.disable()
            }
        }

        return http.build()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwl = RSAKey.Builder(rsaPublicKey)
            .privateKey(rsaPrivateKey)
            .build()
        val jwks = ImmutableJWKSet<SecurityContext>(JWKSet(jwl))
        return NimbusJwtEncoder(jwks)
    }

}