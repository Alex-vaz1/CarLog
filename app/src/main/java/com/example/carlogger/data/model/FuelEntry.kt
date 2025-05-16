package com.example.carlogger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * Representa una carga de nafta.
 */
@Entity(tableName = "fuel_entries")
data class FuelEntry(
   @PrimaryKey(autoGenerate = true)
   val id: Long = 0,

   /** Momento exacto de la carga, en UTC */
   val date: Instant,

   /** Importe total en pesos */
   val amount: Double,

   /** Kilómetros totales que marcaba el odómetro */
   val totalKilometers: Int,

   /** Importe fijo elegido (2000, 3000, etc.) */
   val fixedAmount: Int,

   /** Usuario que cargó (G, A…) */
   val userId: String,

   /** Litros cargados */
   val liters: Double,

   /** Precio por litro usado para el cálculo */
   val pricePerLiter: Double
)
