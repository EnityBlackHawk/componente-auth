package org.example.componenteauth.dto

import org.example.componenteauth.enums.UserRole

data class AuthenticationDto(
    val isValid : Boolean,
    val userType : String,
    val uuid : String
)