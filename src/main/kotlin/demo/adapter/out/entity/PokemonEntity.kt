package demo.adapter.out.entity

import demo.core.data.Pokemon
import javax.persistence.*

@Entity
class PokemonEntity(
    @Id
    val id: String? = null,
    val name: String? = null,
    val types: String? = null,
    val height: String? = null,
    val weight: String? = null,
    val abilities: String? = null,
    val eggGroups: String? = null,
    val stats: String? = null,
    val genus: String? = null,
    val description: String? = null,
) {

    fun toDomainModel(): Pokemon {
        return Pokemon(id, name, types, height, weight, abilities, eggGroups, stats, genus, description)
    }
}


fun Pokemon.toEntity(): PokemonEntity {
    return PokemonEntity(id, name, types, height, weight, abilities, eggGroups, stats, genus, description)
}