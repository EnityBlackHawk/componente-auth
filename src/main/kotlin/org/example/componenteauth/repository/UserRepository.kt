package org.example.componenteauth.repository

import org.example.componenteauth.model.UserCredentials
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<UserCredentials, String> {

    fun findByUsername(username: String): UserCredentials?

}