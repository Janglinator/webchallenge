package demo.core.service

import demo.core.data.Pokemon
import demo.port.`in`.FetchPokemonUseCase
import demo.port.out.PokemonCRUDPort
import org.springframework.stereotype.Component

@Component
class PokemonService(
    private val pokemonCRUDPort: PokemonCRUDPort
): FetchPokemonUseCase {
    override fun fetchPokemon(command: FetchPokemonUseCase.PokemonFetchCommand): List<Pokemon> {
        return pokemonCRUDPort.readPokemon()
    }
//    override fun getAllPokemon(): List<Pokemon> {
//        val pokemon = loadPokemonPort.findPokemon()
//        return pokemon
//    }
}