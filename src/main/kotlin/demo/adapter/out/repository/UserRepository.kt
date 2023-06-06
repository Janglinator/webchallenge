package demo.adapter.out.repository

import demo.adapter.out.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (MongoDB (Document))      MariaDB -> Postgress
// Keycloak
// Microservice
// Websockets
// Quality of Life
// Daba -> TablePlus
// DSL
// LiquidBase
// JIB
// Sonar scan task -> JCoco
// Snyk (locally) & Sonar
// https://antora.org/

// Entity Manager

@Repository
interface UserRepository: JpaRepository<UserEntity, String> {
    fun findByUsername(username: String): UserEntity?
}