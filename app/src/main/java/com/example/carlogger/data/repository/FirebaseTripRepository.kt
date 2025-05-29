package com.example.carlogger.data.repository

import com.example.carlogger.data.model.Trip
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.Instant

class FirebaseTripRepository : TripRepository {

    private val col = Firebase.firestore.collection("trips")

    // Usado en Dashboard (solo últimos 10 viajes)
    override fun getTrips(): Flow<List<Trip>> = callbackFlow {
        val sub = col
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    close(err)
                    return@addSnapshotListener
                }
                val list = snap!!.documents.mapNotNull { doc ->
                    doc.toTrip()
                }
                trySend(list)
            }
        awaitClose { sub.remove() }
    }

    // NUEVO: usado en Home (todos los viajes sin límite)
    fun getAllTrips(): Flow<List<Trip>> = callbackFlow {
        val sub = col
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    close(err)
                    return@addSnapshotListener
                }
                val list = snap!!.documents.mapNotNull { doc ->
                    doc.toTrip()
                }
                trySend(list)
            }
        awaitClose { sub.remove() }
    }

    override suspend fun addTrip(userId: String, last3Digits: Int) {
        val latestSnap = col
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        val prevOdo = latestSnap.documents
            .firstOrNull()
            ?.getLong("odometer")
            ?.toInt() ?: 0

        val prevBlock = prevOdo / 1000
        val prevLast3 = prevOdo % 1000
        val newBlock = if (last3Digits < prevLast3) prevBlock + 1 else prevBlock
        val newOdo = newBlock * 1000 + last3Digits
        val distance = newOdo - prevOdo
        val price = distance * 10.0

        val data = mapOf(
            "userId" to userId,
            "odometer" to newOdo,
            "distanceTraveled" to distance,
            "price" to price,
            "date" to FieldValue.serverTimestamp()
        )
        col.add(data).await()
    }

    override suspend fun deleteTrip(id: Long) {
        col.document(id.toString()).delete().await()
    }

    // Utilidad para mapear documento a Trip
    private fun com.google.firebase.firestore.DocumentSnapshot.toTrip(): Trip? {
        val id = this.id.hashCode().toLong()
        val userId = getString("userId") ?: return null
        val odometer = getLong("odometer")?.toInt() ?: return null
        val distanceTraveled = getLong("distanceTraveled")?.toInt() ?: return null
        val price = getDouble("price") ?: return null
        val date = getTimestamp("date")?.toDate()?.toInstant() ?: Instant.now()

        return Trip(
            id = id,
            userId = userId,
            odometer = odometer,
            distanceTraveled = distanceTraveled,
            price = price,
            date = date
        )
    }
}
