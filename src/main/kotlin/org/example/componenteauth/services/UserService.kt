package org.example.componenteauth.services

import org.example.componenteauth.model.UserCredentials
import org.example.componenteauth.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val bCryptEncoder: BCryptPasswordEncoder
) {

    fun create(user : UserCredentials): UserCredentials {
        val newUser = UserCredentials(
            uuid = user.uuid,
            name = user.name,
            password = bCryptEncoder.encode(user.password),
            email = user.email,
            userType = user.userType
        )
        return userRepository.save(newUser)
    }

    fun findByEmail(email: String): UserCredentials? {
        return userRepository.findByEmail(email)
    }


}