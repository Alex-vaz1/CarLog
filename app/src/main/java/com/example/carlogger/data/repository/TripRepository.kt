package com.example.carlogger.data.repository

import com.example.carlogger.data.model.Trip
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de persistencia de Trips.
 * Puede estar implementado sobre Room o Firestore.
 */
interface TripRepository {
    /** Devuelve todos los trips ordenados por fecha descendente */
    fun getTrips(): Flow<List<Trip>>

    /** Inserta un Trip dado userId y últimos 3 dígitos de odómetro */
    suspend fun addTrip(userId: String, last3Digits: Int)

    /** Elimina un Trip dado su id */
    suspend fun deleteTrip(id: Long)
}
