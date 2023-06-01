package demo.adapter.out

import demo.adapter.out.dao.PokemonDao
import demo.adapter.out.entity.toEntity
import demo.core.data.Pokemon
import demo.port.out.PokemonCRUDPort
import org.springframework.stereotype.Repository

@Repository
class PokemonRepository(
    val pokemonDao: PokemonDao,
): PokemonCRUDPort {
    //    override fun findPokemon(): List<Pokemon> {
//        return pokemonRepository.findPokemon()
//    }
    override fun createPokemon(pokemon: List<Pokemon>) {
//        TODO("Not yet implemented")
        pokemonDao.saveAll(pokemon.map { it.toEntity() })
    }

    override fun readPokemon(): List<Pokemon> {
//        TODO("Not yet implemented")
        return pokemonDao.findAll().map { it.toDomainModel() }
//        return listOf()
    }
}