package org.example.componenteauth.services

import org.example.componenteauth.model.UserCredentials
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class UserQueuedService(private val userService: UserService) {

    @RabbitListener(queues = ["auth-user-create"])
    fun create(user: UserCredentials) {
        print(user)
        userService.create(user)
    }

}
