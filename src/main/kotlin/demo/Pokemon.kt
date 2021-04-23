package demo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("POKEMON")
data class Pokemon (
    @Id val id: String?,
    val pokemonId: String?,
    val name: String?,
    val types: String?,
    val height: String?,
    val weight: String?,
    val abilities: String?,
    val eggGroups: String?,
    val stats: String?,
    val genus: String?,
    val description: String?,
    var captured: Boolean
)

data class PokemonResponse (
    val data: List<Pokemon>,
    val meta: PokemonResponseMeta
)

data class PokemonResponseMeta (
    val results: Int,
    val page: Int,
    val pageCount: Int,
    val perPage: Int
)