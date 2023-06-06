package demo.adapter.out.dao

import demo.adapter.out.repository.PokemonRepository
import demo.adapter.out.entity.toEntity
import demo.core.data.Pokemon
import demo.port.out.PokemonCRUDPort
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class PokemonDao(
    val pokemonRepository: PokemonRepository,
): PokemonCRUDPort {
    override fun createPokemon(pokemon: List<Pokemon>) {
        pokemonRepository.saveAll(pokemon.map { it.toEntity() })
    }

    override fun readPokemon(): List<Pokemon> {
        return pokemonRepository.findAll().map { it.toDomainModel() }
    }
}