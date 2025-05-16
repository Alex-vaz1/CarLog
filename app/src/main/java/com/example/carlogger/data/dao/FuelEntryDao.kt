package com.example.carlogger.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.carlogger.data.model.FuelEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelEntryDao {

    /** Cada vez que cambie la tabla, emitimos la lista ordenada por fecha descendente */
    @Query("SELECT * FROM fuel_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<FuelEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FuelEntry)

    @Query("DELETE FROM fuel_entries")
    suspend fun clearAll()
}
