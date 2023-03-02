package com.zamolski.rainfallmeasurer

import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class MeasureStore {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun save(data: String) {
        val storage: Storage = StorageOptions.getDefaultInstance().service
        val bucketName = System.getenv()[BUCKET_NAME_CODE]
        val bucket: Bucket = storage.get(bucketName)
        val fileName = generateFileName()
        bucket.create(fileName, data.toByteArray(StandardCharsets.UTF_8))
        logger.info("new measures file stored: $fileName in $bucketName bucket")

    }

    private fun generateFileName(): String {
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd_HH-mm-ss-SSSSSS")
            .withZone(ZoneId.of("Europe/Warsaw"))
            .format(Instant.now()) + ".csv"
    }
}