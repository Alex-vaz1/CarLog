package com.example.carlogger.data

import androidx.room.TypeConverter
import java.time.Instant

/**
 * Convierte Instant <–> Long para que Room pueda persistirlo.
 */
class Converters {
    @TypeConverter
    fun fromEpoch(epochMilli: Long?): Instant? =
        epochMilli?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun toEpoch(instant: Instant?): Long? =
        instant?.toEpochMilli()
}
