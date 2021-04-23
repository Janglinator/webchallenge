package demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.lang.Math.ceil
import java.lang.Math.min
import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*


@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

@RestController
@RequestMapping("api/v1/user")
class UserController(val userService: UserService) {
	@PostMapping("/register")
	fun registerUser(
		@RequestBody registration: UserRegistration
	) {
		val existing = userService.findUser(registration.username)

		if (existing != null) {
			throw ResponseStatusException(
				HttpStatus.CONFLICT, "User already exists", null
			)
		}

		val user = User(null, registration.username, registration.password)
		userService.post(user)
	}

	@PostMapping("/changePassword")
	fun changePassword(
		@RequestHeader("Authorization") authorization: String,
		@RequestBody newPassword: String
	) {
		val user = userService.validateUser(authorization)
		user.password = newPassword
		userService.post(user)
	}

	@PostMapping("/changeUsername")
	fun changeUsername(
		@RequestHeader("Authorization") authorization: String,
		@RequestBody newUsername: String
	) {
		val user = userService.validateUser(authorization)
		user.username = newUsername
		userService.post(user)
	}
}

@RestController
@RequestMapping("/api/v1/pokemon")
class PokemonController(val userService: UserService, val pokemonService: PokemonService) {
	@GetMapping
	fun getPokemon(
		@RequestParam(value = "search") search: String?,
		@RequestParam(value = "perPage", defaultValue = "20") perPage: Int,
		@RequestParam(value = "page", defaultValue = "1") page: Int,
		@RequestHeader("Authorization") authorization: String
	): PokemonResponse {
		return getPokemon(search, perPage, page, authorization, true)
	}

	@GetMapping(value = ["/captured"])
	fun getPokemon(
		@RequestParam(value = "search") search: String?,
		@RequestParam(value = "perPage", defaultValue = "20") perPage: Int,
		@RequestParam(value = "page", defaultValue = "1") page: Int,
		@RequestHeader("Authorization") authorization: String,
		showUncaptured: Boolean = false
	): PokemonResponse {
		val user = userService.validateUser(authorization)
		var pokemon = pokemonService.findPokemon(user)

		if (!showUncaptured) {
			pokemon = pokemon.filter { it.captured }
		}

		search?.let {
			pokemon = pokemon.filter {
				it.name?.toLowerCase()?.contains(search?.toLowerCase() ?: "") ?: false
			}
		}

		var paginatedPokemon = listOf<UserPokemon>()
		val start = perPage * (page - 1)
		val end = min(perPage * page, pokemon.count())
		if (start <= pokemon.count() && start >= 0) {
			paginatedPokemon = pokemon.subList(start, end)
		}

		return PokemonResponse(
			paginatedPokemon,
			PokemonResponseMeta(
				pokemon.count(),
				page,
				ceil(pokemon.count().toDouble()/perPage).toInt(),
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

@Repository
interface UserRepository : CrudRepository<User, String> {
	@Query("select * from user")
	fun findUsers(): List<User>
}

@Service
class UserService(val db: UserRepository) {
	// TODO: Use spring boot's security library.  It's too complex for a hack-n-slash demo like this.
	fun validateUser(authorization: String): User {
		var username: String? = null
		var password: String? = null

		if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
			val base64Credentials = authorization.substring("Basic".length).trim()
			val credDecoded: ByteArray = Base64.getDecoder().decode(base64Credentials)
			val credentials = String(credDecoded, StandardCharsets.UTF_8)
			val values = credentials.split(":")
			username = values[0]
			password = values[1]
		}

		var user = findUser(username ?: "")

		if (user == null || user?.password != password) {
			throw ResponseStatusException(
				HttpStatus.UNAUTHORIZED, "User not found", null
			)
		}

		return user!!
	}

	fun findUser(userName: String): User? {
		return db.findUsers().firstOrNull { it.username == userName }
	}

	fun post(user: User) {
		db.save(user)
	}
}

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