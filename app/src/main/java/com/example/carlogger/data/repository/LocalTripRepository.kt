package com.example.carlogger.data.repository

import com.example.carlogger.data.dao.TripDao
import com.example.carlogger.data.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.Instant

class LocalTripRepository(
    private val dao: TripDao
) : TripRepository {

    companion object {
        private const val PRICE_PER_KM = 10.0
    }

    override fun getTrips(): Flow<List<Trip>> =
        dao.getAllTrips()

    override suspend fun addTrip(userId: String, last3Digits: Int) {
        // Lógica de cálculo de odómetro y distancia (con rollover de miles)
        val latest = dao.getLatestTrip()
        val prevOdo = latest?.odometer ?: 0
        val prevBlock = prevOdo / 1000
        val prevLast3 = prevOdo % 1000

        val newBlock = if (last3Digits < prevLast3) prevBlock + 1 else prevBlock
        val newOdo = newBlock * 1000 + last3Digits
        val distance = newOdo - prevOdo
        val price = distance * PRICE_PER_KM
        val trip = Trip(
            userId = userId,
            odometer = newOdo,
            distanceTraveled = distance,
            price = price,
            date = Instant.now()
        )
        dao.insert(trip)
    }

    override suspend fun deleteTrip(id: Long) {
        dao.deleteById(id)
    }
}
