package org.example.componenteauth.model

import org.example.componenteauth.enums.UserRole
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class UserCredentials(
    val uuid: String,
    val username: String,
    val password: String,
    val roles: List<UserRole>
)
