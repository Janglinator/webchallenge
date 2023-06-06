package demo.adapter.out.dao

import demo.core.data.Pokemon
import demo.port.out.CSVDownloadPort
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Error
import java.net.URL

@Component
class CSVDownloadDao: CSVDownloadPort {
    override fun downloadPokemon(onComplete: (List<Pokemon>?, Error?) -> Unit) {
        val csvPath = "https://bitbucket.org/!api/2.0/snippets/myriadmobile/Rerr8E/96d04ea30f8e177149dd0c1c98271f1843b5f9b7/files/pokedex.csv"
        URL(csvPath).openStream().use { inputStream ->
            val records = parseStream(inputStream)
            val pokemon = serializeRecords(records)
            onComplete(pokemon, null)
        }
    }

    fun parseStream(stream: InputStream): List<CSVRecord> {
        val reader =  BufferedReader(InputStreamReader(stream, "UTF-8"))
        val parser = CSVParser(reader, CSVFormat.EXCEL.withHeader())
        return parser.records
    }

    fun serializeRecords(records: List<CSVRecord>): List<Pokemon> {
        val pokemon = mutableListOf<Pokemon>()
        for (record in records) {
            pokemon.add(
                Pokemon(
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
            )
        }

        return pokemon
    }
}