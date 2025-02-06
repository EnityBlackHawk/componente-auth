package org.example.componenteauth

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import java.security.interfaces.RSAPublicKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.config.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
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
@EnableMethodSecurity
open class SecurityConfig(
    @Value("\${jwt.public.key}") val rsaPublicKey: RSAPublicKey,
    @Value("\${jwt.private.key}") val rsaPrivateKey: RSAPrivateKey
) {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { auth ->
            auth.requestMatchers("/product/getAll").permitAll()
                .requestMatchers("/product/findMany").permitAll()
                .requestMatchers("/login").permitAll()
                .anyRequest().permitAll()
        }

        http.csrf {
            it.disable()
        }
        http.oauth2ResourceServer { it.jwt { jwtDecoder() } }
        http.headers { h ->
            h.frameOptions {
                it.disable()
            }
        }

        return http.build()
    }

    @Bean
    open fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    open fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build()
    }

    @Bean
    open fun jwtEncoder(): JwtEncoder {
        val jwl = RSAKey.Builder(rsaPublicKey)
            .privateKey(rsaPrivateKey)
            .build()
        val jwks = ImmutableJWKSet<SecurityContext>(JWKSet(jwl))
        return NimbusJwtEncoder(jwks)
    }

}