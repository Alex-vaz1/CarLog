package com.example.carlogger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.IgnoreExtraProperties
import java.time.Instant

@IgnoreExtraProperties
@Entity(tableName = "trips")
data class Trip(
    /** Room autogenera el ID */
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    /** Usuario que hizo el viaje */
    var userId: String = "",

    /** Odómetro total al momento del viaje */
    var odometer: Int = 0,

    /** Distancia recorrida (se calcula en el repo) */
    var distanceTraveled: Int = 0,

    /** Precio del recorrido (se calcula en el repo) */
    var price: Double = 0.0,

    /** Fecha del viaje; por defecto ‘ahora’ */
    var date: Instant = Instant.now()
)
