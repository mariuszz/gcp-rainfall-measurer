package com.zamolski.rainfallmeasurer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.xml.sax.InputSource

class MeasureFetcherTest {

    @Test
    fun getCsvUrlTest() {
        val fetcher = MeasureFetcher()
        val csvUrl = fetcher.parseRdfFile(InputSource(this::class.java.getResourceAsStream("/com/zamolski/rainfallmeasurer/test_rdf.xml")))
        assertEquals("https://www.wroclaw.pl/open-data/f91dd592-95fe-416f-a43e-97838fbb0147/Deszczomierze.csv", csvUrl)
    }

}