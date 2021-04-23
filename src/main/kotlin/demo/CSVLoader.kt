package demo

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class CSVLoader(
    private val service: PokemonService
    ) {

    public fun loadPokemon(stream: InputStream) {
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        val parser = CSVParser(reader, CSVFormat.EXCEL.withHeader())
        val records = parser.getRecords()

        for (record in records) {
            val pokemon = Pokemon(
                null,
                record.get("id"),
                record.get("name"),
                record.get("types"),
                record.get("height"),
                record.get("weight"),
                record.get("abilities"),
                record.get("egg_groups"),
                record.get("stats"),
                record.get("genus"),
                record.get("description")
            )

            service.post(pokemon)
        }
    }
}