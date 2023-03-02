package com.zamolski.rainfallmeasurer

import com.google.cloud.functions.BackgroundFunction
import com.google.cloud.functions.Context
import java.util.*

const val BUCKET_NAME_CODE = "BUCKET_NAME"
const val RDF_URL_CODE = "RDF_URL"
const val PING_FETCH_MESSAGE = "fetch"

class FetchFunction : BackgroundFunction<Ping> {

    override fun accept(payload: Ping?, context: Context?) {
        if (Base64.getDecoder().decode(payload?.data).decodeToString() == PING_FETCH_MESSAGE) {
            MeasureFetcher().fetchMeasure()
        }
    }
}