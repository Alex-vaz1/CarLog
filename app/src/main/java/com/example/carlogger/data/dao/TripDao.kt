package com.example.carlogger.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.carlogger.data.model.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    @Query("SELECT * FROM trips ORDER BY date DESC")
    fun getAllTrips(): Flow<List<Trip>>

    @Query("SELECT * FROM trips ORDER BY date DESC LIMIT 1")
    suspend fun getLatestTrip(): Trip?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: Trip)

    @Query("DELETE FROM trips")
    suspend fun clearAll()

    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteById(tripId: Long)
}
