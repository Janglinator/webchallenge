package demo.port.`in`

import demo.core.data.Pokemon

interface FetchPokemonUseCase {
    fun fetchPokemon(command: PokemonFetchCommand): List<Pokemon>

    data class PokemonFetchCommand(
        val itemsPerPage: Int,
        val page: Int = 1,
        val query: String?,
    )
}