package _old.controller

import _old.model.PokemonResponse
import _old.model.PokemonResponseMeta
import _old.model.UserPokemon
import org.springframework.web.bind.annotation.*
import _old.service.PokemonService
import _old.service.UserService

@RestController
@RequestMapping("/api/v1/pokemon")
class PokemonController(val userService: UserService, val pokemonService: PokemonService) {
    @GetMapping
    fun getPokemon(
        @RequestParam(value = "search") search: String?,
        @RequestParam(value = "perPage", defaultValue = "20") perPage: Int,
        @RequestParam(value = "page", defaultValue = "1") page: Int,
        @RequestParam(value = "captured") captured: String?,
        @RequestHeader("Authorization") authorization: String
    ): PokemonResponse {
        val user = userService.validateUser(authorization)
        var pokemon = pokemonService.findPokemon(user)

        if (captured?.toLowerCase() == "true") {
            pokemon = pokemon.filter { it.captured }
        } else if (captured?.toLowerCase() == "false") {
            pokemon = pokemon.filter { !it.captured }
        }

        search?.let {
            pokemon = pokemon.filter {
                it.name?.toLowerCase()?.contains(search?.toLowerCase() ?: "") ?: falseD
            }
        }

        var paginatedPokemon = listOf<UserPokemon>()
        val start = perPage * (page - 1)
        val end = Math.min(perPage * page, pokemon.count())
        if (start <= pokemon.count() && start >= 0) {
            paginatedPokemon = pokemon.subList(start, end)
        }

        return PokemonResponse(
            paginatedPokemon,
            PokemonResponseMeta(
                pokemon.count(),
                page,
                Math.ceil(pokemon.count().toDouble() / perPage).toInt(),
                perPage
            )
        )
    }

    @GetMapping(value = ["/{id}"])
    fun getPokemon(
        @PathVariable id: Int,
        @RequestHeader("Authorization") authorization: String
    ): UserPokemon? {
        val user = userService.validateUser(authorization)
        return pokemonService.findPokemon(user, id.toString())
    }

    @PostMapping(value = ["capture/{id}"])
    fun capturePokemon(
        @PathVariable id: Int,
        @RequestHeader("Authorization") authorization: String
    ): UserPokemon? {
        val user = userService.validateUser(authorization)
        val pokemon = pokemonService.findPokemon(user).firstOrNull {
            it.pokedexId == id.toString()
        }

        pokemon?.captured = true
        pokemon?.let {
            pokemonService.capture(user, it)
        }

        return pokemon
    }
}