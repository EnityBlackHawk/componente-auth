package org.example.componenteauth.controller


import org.example.componenteauth.dto.AuthenticationDto
import org.example.componenteauth.dto.LoginRequest
import org.example.componenteauth.dto.LoginResponse
import org.example.componenteauth.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class TokenController(
    private val userService: UserService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtEncoder: JwtEncoder
) {

    @PostMapping("/login")
    fun login(@RequestBody request : LoginRequest) : ResponseEntity<LoginResponse> {
        var user = userService.findByEmail(request.username)
        if(user == null || !bCryptPasswordEncoder.matches(request.password, user.password)) {
            throw BadCredentialsException("user or password invalid")
        }
        val now = Instant.now()
        val expiresIn = 300L

        var claims = JwtClaimsSet.builder()
            .issuer("com.sistema")
            .subject(user.uuid)
            .expiresAt(now.plusSeconds(expiresIn))
            .issuedAt(now)
            .claim("scope", user.userType)
            .build()

        val jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue

        return ResponseEntity.ok(LoginResponse(jwt, expiresIn))
    }

    @PostMapping("/authenticate")
    fun authenticate(token : JwtAuthenticationToken?, @RequestHeader headers : Map<String, String>) : ResponseEntity<AuthenticationDto> {

        if(token == null) {
            return ResponseEntity.status(401).build()
        }

        val auth = AuthenticationDto(
            uuid = token.name,
            userType = token.tokenAttributes["scope"] as String,
            isValid = true,
        )

        return ResponseEntity.ok(auth)
    }

}