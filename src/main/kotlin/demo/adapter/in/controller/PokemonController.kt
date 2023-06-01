package demo.adapter.`in`.controller

import demo.adapter.`in`.controller.response.PokemonResponse
import org.springframework.web.bind.annotation.*
import demo.port.`in`.FetchPokemonUseCase

@RestController
@RequestMapping("/api/v1/pokemon")
class PokemonController(
    private val fetchPokemonUseCase: FetchPokemonUseCase
) {
    @GetMapping
    fun getPokemon(
        @RequestParam(value = "search") search: String?,
        @RequestParam(value = "perPage", defaultValue = "20") perPage: Int,
        @RequestParam(value = "page", defaultValue = "1") page: Int,
        @RequestParam(value = "captured") captured: String?,
        @RequestHeader("Authorization") authorization: String
    ): PokemonResponse {
        val command = FetchPokemonUseCase.PokemonFetchCommand(
            itemsPerPage = perPage,
            page = page,
            query = search,
        )
        val pokemon = fetchPokemonUseCase.fetchPokemon(command)
        return PokemonResponse(
            pokemon,
        )

//        val user = userService.validateUser(authorization)
//        var pokemon = pokemonService.findPokemon(user)
//
//        if (captured?.toLowerCase() == "true") {
//            pokemon = pokemon.filter { it.captured }
//        } else if (captured?.toLowerCase() == "false") {
//            pokemon = pokemon.filter { !it.captured }
//        }
//
//        search?.let {
//            pokemon = pokemon.filter {
//                it.name?.toLowerCase()?.contains(search?.toLowerCase() ?: "") ?: false
//            }
//        }
//
//        var paginatedPokemon = listOf<UserPokemon>()
//        val start = perPage * (page - 1)
//        val end = Math.min(perPage * page, pokemon.count())
//        if (start <= pokemon.count() && start >= 0) {
//            paginatedPokemon = pokemon.subList(start, end)
//        }
//
//        return PokemonResponse(
//            paginatedPokemon,
//            PokemonResponseMeta(
//                pokemon.count(),
//                page,
//                Math.ceil(pokemon.count().toDouble() / perPage).toInt(),
//                perPage
//            )
//        )
    }
}