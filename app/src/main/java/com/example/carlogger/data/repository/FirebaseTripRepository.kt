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

    private val col = Firebase.firestore
        .collection("trips")

    override fun getTrips(): Flow<List<Trip>> = callbackFlow {
        val sub = col
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    close(err)
                    return@addSnapshotListener
                }
                val list = snap!!.documents.mapNotNull { doc ->
                    // ID numérico
                    val id = doc.id.toLongOrNull() ?: return@mapNotNull null
                    // Campos básicos
                    val userId = doc.getString("userId") ?: return@mapNotNull null
                    val odometer = doc.getLong("odometer")?.toInt() ?: 0
                    val distanceTraveled = doc.getLong("distanceTraveled")?.toInt() ?: 0
                    val price = doc.getDouble("price") ?: 0.0
                    // Fecha: convertir Timestamp → Date → Instant
                    val ts = doc.getTimestamp("date")
                    val date = ts?.toDate()?.toInstant() ?: Instant.now()
                    Trip(
                        id = id,
                        userId = userId,
                        odometer = odometer,
                        distanceTraveled = distanceTraveled,
                        price = price,
                        date = date
                    )
                }
                trySend(list)
            }
        awaitClose { sub.remove() }
    }

    override suspend fun addTrip(userId: String, last3Digits: Int) {
        // Mismo cálculo que en LocalTripRepository
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
}
