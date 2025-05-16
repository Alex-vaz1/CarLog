package com.example.carlogger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * Representa un viaje realizado.
 */
@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Usuario que hizo el viaje (G, A…) */
    val userId: String,

    /** Kilómetros recorridos en este viaje */
    val distanceTraveled: Int,

    /** Coste total del viaje (distance * precio/km) */
    val price: Double,

    /** Lectura completa del odómetro al final del viaje */
    val odometer: Int,

    /** Momento exacto del viaje */
    val date: Instant
)