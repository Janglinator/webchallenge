package _old.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("POKEMON")
data class Pokemon (
    @Id val id: String?,
    val pokedexId: String?,
    val name: String?,
    val types: String?,
    val height: String?,
    val weight: String?,
    val abilities: String?,
    val eggGroups: String?,
    val stats: String?,
    val genus: String?,
    val description: String?,
)

open class BasePokemon(
    val pokedexId: String?,
    val name: String?,
    val types: String?,
    val height: String?,
    val weight: String?,
    val abilities: String?,
    val eggGroups: String?,
    val stats: String?,
    val genus: String?,
    val description: String?,
)

data class PokemonResponseMeta (
    val results: Int,
    val page: Int,
    val pageCount: Int,
    val perPage: Int
)

@Table("USER_TO_POKEMON")
data class UserToPokemon (
    @Id val id: String?,
    val userId: String?,
    val pokedexId: String?
)

class UserPokemon (
    var captured: Boolean,
    pokedexId: String?,
    name: String?,
    types: String?,
    height: String?,
    weight: String?,
    abilities: String?,
    eggGroups: String?,
    stats: String?,
    genus: String?,
    description: String?,
): BasePokemon(pokedexId, name, types, height, weight, abilities, eggGroups, stats, genus, description) {
    constructor(captured: Boolean, p: Pokemon)
            : this(captured, p.pokedexId, p.name, p.types, p.height, p.weight, p.abilities, p.eggGroups, p.stats, p.genus, p.description)
}