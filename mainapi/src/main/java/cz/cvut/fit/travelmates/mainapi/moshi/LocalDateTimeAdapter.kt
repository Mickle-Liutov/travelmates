package cz.cvut.fit.travelmates.mainapi.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Moshi adapter for converting LocalDate and LocalDateTime
 */
object LocalDateTimeAdapter {

    private val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val FORMATTER_TIME = DateTimeFormatter.ISO_LOCAL_DATE

    @ToJson
    fun toJsonTime(value: LocalDateTime): String {
        return FORMATTER.format(value)
    }

    @FromJson
    fun fromJsonTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value, FORMATTER)
    }

    @ToJson
    fun toJsonDate(value: LocalDate): String {
        return FORMATTER_TIME.format(value)
    }

    @FromJson
    fun fromJsonDate(value: String): LocalDate {
        return LocalDate.parse(value, FORMATTER_TIME)
    }

}