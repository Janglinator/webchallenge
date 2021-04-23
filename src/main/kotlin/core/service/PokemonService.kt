package core.service

import core.model.Pokemon
import core.model.User
import core.model.UserPokemon
import core.model.UserToPokemon
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.net.URL

@Repository
interface PokemonRepository : CrudRepository<Pokemon, String> {
    @Query("select * from pokemon")
    fun findPokemon(): List<Pokemon>
}

@Repository
interface UserToPokemonRepository : CrudRepository<UserToPokemon, String> {
    @Query("select * from user_to_pokemon")
    fun findUserPokemon(): List<UserToPokemon>
}

@Service
class PokemonService(val db: PokemonRepository, val userToPokemondb: UserToPokemonRepository) {
    fun findPokemon(user: User, id: String): UserPokemon? {
        return findPokemon(user).firstOrNull {
            it.pokedexId == id
        }
    }

    fun findPokemon(user: User): List<UserPokemon> {
        var pokemon = db.findPokemon()

        // TODO: preload on application launch
        if (pokemon.count() == 0) {
            val csvPath = "https://bitbucket.org/!api/2.0/snippets/myriadmobile/Rerr8E/96d04ea30f8e177149dd0c1c98271f1843b5f9b7/files/pokedex.csv"
            URL(csvPath).openStream().use { input ->
                CSVLoader(this).loadPokemon(input)
                pokemon = db.findPokemon()
            }
        }

        val userToPokemon = userToPokemondb.findUserPokemon().filter { it.userId == user.id }
        val userPokemon = pokemon.map { pokemon -> UserPokemon(userToPokemon.any { it.pokedexId == pokemon.pokedexId }, pokemon) }

        return userPokemon
    }

    fun post(pokemon: Pokemon) {
        db.save(pokemon)
    }

    fun capture(user: User, pokemon: UserPokemon) {
        val entry = UserToPokemon(null, user.id, pokemon.pokedexId)
        userToPokemondb.save(entry)
    }
}