package demo

import demo.config.JwtAuthenticationFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@SpringBootApplication
@EnableWebSecurity
class DemoApplication(val userDetailsService: UserDetailsService): WebSecurityConfigurerAdapter() {
	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
	}

	override fun configure(http: HttpSecurity) {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/register", "/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(
				JwtAuthenticationFilter(authenticationManager()),
				UsernamePasswordAuthenticationFilter::class.java
			)
	}

	@Bean
	override fun authenticationManager(): AuthenticationManager {
		return super.authenticationManager()
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}