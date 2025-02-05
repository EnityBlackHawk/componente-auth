package org.example.componenteauth.dto

data class LoginResponse(
    val token: String,
    val expiresIn: Long
)
