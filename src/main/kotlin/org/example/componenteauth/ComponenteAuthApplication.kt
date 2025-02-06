package org.example.componenteauth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class ComponenteAuthApplication {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ComponenteAuthApplication>(*args)
        }
    }




}



