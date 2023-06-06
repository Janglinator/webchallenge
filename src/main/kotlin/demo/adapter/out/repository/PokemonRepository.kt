package demo.adapter.out.repository

import demo.adapter.out.entity.PokemonEntity
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
// Working with SQL
// Get Azdo deployment setup
// All in TCP  rancher.bushelops.com
// Kubernetes

@Repository
interface PokemonRepository: JpaRepository<PokemonEntity, String>