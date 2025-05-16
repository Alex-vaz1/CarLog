package com.example.carlogger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Datos agregados por usuario para mostrar estadísticas generales.
 */
@Entity(tableName = "users")
data class User(
    /** Clave: única, una sola letra */
    @PrimaryKey
    val id: String,

    /** Kilómetros totales acumulados de todos sus viajes */
    val totalKilometers: Int = 0,

    /** % de uso sobre el total del auto */
    val usagePercentage: Double = 0.0
)
