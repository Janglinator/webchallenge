package demo.port.out

import demo.core.data.Pokemon
import java.lang.Error

interface CSVDownloadPort {
    fun downloadPokemon(onComplete: (List<Pokemon>?, Error?) -> Unit)
}