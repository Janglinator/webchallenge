package demo.core.service

import demo.port.out.CSVDownloadPort
import demo.port.out.PokemonCRUDPort
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AppStartService(
    val cSVDownloadPort: CSVDownloadPort,
    val pokemonCRUDPort: PokemonCRUDPort,
): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        cSVDownloadPort.downloadPokemon { pokemon, error ->
            error?.let {
                println("ERROR: $it")
                return@downloadPokemon
            }

            pokemon?.let {
                pokemonCRUDPort.createPokemon(it)
            }
        }
    }
}