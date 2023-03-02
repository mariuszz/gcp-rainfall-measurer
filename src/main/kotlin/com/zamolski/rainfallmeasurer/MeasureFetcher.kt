package com.zamolski.rainfallmeasurer

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class MeasureFetcher {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val measureStore = MeasureStore()

    fun fetchMeasure() {
        logger.info("env vars: ${System.getenv()}")
        fetchData(System.getenv()[RDF_URL_CODE], ::rdfFileHandler)
    }

    private fun rdfFileHandler(rdfFileContent: String) {
        val csvUrl = parseRdfFile(InputSource(StringReader(rdfFileContent)))
        fetchData(csvUrl, ::csvFileHandler)
    }
    private fun csvFileHandler(csvFileContent: String) {
        measureStore.save(csvFileContent)
    }

    fun parseRdfFile(rdfFileSource: InputSource): String {
        val document = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().parse(rdfFileSource)
        return document.documentElement
            .getElementsByTagName("dcat:accessURL").item(0)
            .attributes.getNamedItem("rdf:resource").nodeValue
    }

    private fun fetchData(url: String?, processFunction:(bodyAsString: String) -> Unit) {
        if (url.isNullOrBlank()) {
            logger.error("url can't be empty")
            return
        }
        runBlocking {
            launch {
                val client = HttpClient(CIO)
                kotlin.runCatching {
                    logger.info("trying to fetch $url")
                    client.get(url)
                }.fold(
                    {
                        logger.info("success ${it.status}")
                        processFunction(it.bodyAsText())
                    },
                    {
                        logger.error("error while fetching data", it)
                    }
                )
                client.close()
            }
        }
    }

}