package demo

import User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.lang.Math.ceil
import java.lang.Math.min
import java.net.URL
import java.util.*


@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

@Component
class CustomAuthenticationProvider : AuthenticationProvider {
	@Autowired
	private val userService: UserService? = null

	@Throws(AuthenticationException::class)
	override fun authenticate(authentication: Authentication): Authentication {
		val email: String = authentication.name
		val hashedPassword = BCryptPasswordEncoder().encode(authentication.credentials.toString())
//		val user: User = userService!!.findUser(email)
		val authorities: MutableList<GrantedAuthority> = ArrayList<GrantedAuthority>()
		authorities.add(SimpleGrantedAuthority("USER"))
		return UsernamePasswordAuthenticationToken(email, hashedPassword, authorities)
	}

	override fun supports(authentication: Class<*>): Boolean {
		return authentication == UsernamePasswordAuthenticationToken::class.java
	}
}

@Repository
interface UserRepository : CrudRepository<User, String> {
	@Query("select * from user")
	fun findUser(username: String): User
}

@Service
class UserService(val db: UserRepository) {
	fun findUser(userName: String): User {
		return db.findUser(userName)
	}

//	fun post(pokemon: Pokemon) {
//		db.save(pokemon)
//	}
}

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {
	@Autowired
	private val authProvider: CustomAuthenticationProvider? = null
	@Autowired
	@Throws(Exception::class)
	fun configAuthentication(auth: AuthenticationManagerBuilder) {
		auth.authenticationProvider(authProvider)
	}

	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		http.httpBasic()
			.and().csrf().disable().headers().frameOptions().disable()
			.and().authorizeRequests()
			.anyRequest().hasAnyRole("USER")
//		http.httpBasic()
//			.and().authorizeRequests().antMatchers("/**").hasRole("USER")
//			.and().csrf().disable().headers().frameOptions().disable()
//		http.csrf().disable().authorizeRequests()
//			.anyRequest().authenticated()
//			.and().httpBasic()
//			.authenticationEntryPoint(authEntryPoint);
	}
}

//@Configuration
//@EnableWebSecurity
//class CustomWebSecurityConfigurerAdapter : WebSecurityConfigurerAdapter() {
//	@Autowired
//	@Throws(java.lang.Exception::class)
//	fun configureGlobal(auth: AuthenticationManagerBuilder) {
//		auth.inMemoryAuthentication()
//			.withUser("user1")
//			.password(
//				passwordEncoder()
//					.encode("user1Pass")
//			)
//			.authorities("ROLE_USER")
//	}
//
//	@Throws(java.lang.Exception::class)
//	override fun configure(http: HttpSecurity) {
//		http.authorizeRequests()
//			.anyRequest().authenticated()
//			.and()
//			.httpBasic()
//	}
//
//	@Bean
//	fun passwordEncoder(): PasswordEncoder {
//		return BCryptPasswordEncoder()
//	}
//}

@RestController
@RequestMapping("/api/v1/pokemon")
class PokemonController(val service: PokemonService) {
	@GetMapping
	fun getPokemon(
		@RequestParam(value = "search") search: String?,
		@RequestParam(value = "perPage", defaultValue = "20") perPage: Int,
		@RequestParam(value = "page", defaultValue = "1") page: Int
	): PokemonResponse {
		return getPokemon(search, perPage, page, true)
	}

	@GetMapping(value = ["/captured"])
	fun getPokemon(
		@RequestParam(value = "search") search: String?,
		@RequestParam(value = "perPage", defaultValue = "20") perPage: Int,
		@RequestParam(value = "page", defaultValue = "1") page: Int,
		showUncaptured: Boolean = false
	): PokemonResponse {
		var pokemon = service.findPokemon()

		if (!showUncaptured) {
			pokemon = pokemon.filter { it.captured }
		}

		search?.let {
			pokemon = pokemon.filter {
				it.name?.toLowerCase()?.contains(search?.toLowerCase() ?: "") ?: false
			}
		}

		var paginatedPokemon = listOf<Pokemon>()
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
	fun getPokemon(@PathVariable id: Int): Pokemon? {
		return service.findPokemon().firstOrNull {
			it.pokemonId == id.toString()
		}
	}

	@PostMapping(value = ["capture/{id}"])
	fun capturePokemon(@PathVariable id: Int): Pokemon? {
		val pokemon = service.findPokemon().firstOrNull {
			it.pokemonId == id.toString()
		}

		pokemon?.captured = true
		pokemon?.let {
			service.post(it)
		}

		return pokemon
	}
}

@Repository
interface PokemonRepository : CrudRepository<Pokemon, String> {
	@Query("select * from pokemon")
	fun findPokemon(): List<Pokemon>
}

@Service
class PokemonService(val db: PokemonRepository) {
	fun findPokemon(): List<Pokemon> {
		var pokemon = db.findPokemon()

		if (pokemon.count() == 0) {
			val csvPath = "https://bitbucket.org/!api/2.0/snippets/myriadmobile/Rerr8E/96d04ea30f8e177149dd0c1c98271f1843b5f9b7/files/pokedex.csv"
			URL(csvPath).openStream().use { input ->
				CSVLoader(this).loadPokemon(input)
				pokemon = db.findPokemon()
			}
		}

		return pokemon
	}

    fun post(pokemon: Pokemon) {
        db.save(pokemon)
	}
}