package demo.port.out

import demo.core.data.Pokemon

interface PokemonCRUDPort {
    fun createPokemon(pokemon: List<Pokemon>)
    fun readPokemon(): List<Pokemon>
}