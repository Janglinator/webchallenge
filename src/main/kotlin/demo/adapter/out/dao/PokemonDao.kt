package demo.adapter.out.dao

import demo.adapter.out.entity.PokemonEntity
import demo.core.data.Pokemon
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository
import java.util.*

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

// Entity Manager

interface PokemonDao: JpaRepository<PokemonEntity, String>