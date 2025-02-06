package org.example.componenteauth.model

import org.example.componenteauth.enums.UserRole
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class UserCredentials(
    val uuid: String,
    val name: String,
    val email: String,
    val password: String,
    val userType: UserRole
)
