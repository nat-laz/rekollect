package org.example.rekollectapi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan

@SpringBootApplication
@EntityScan("com.example.rekollectapi.models.entities") // Ensure JPA scans the entities
class RekollectApiApplication {

    static void main(String[] args) {
        SpringApplication.run(RekollectApiApplication, args)
    }

}
