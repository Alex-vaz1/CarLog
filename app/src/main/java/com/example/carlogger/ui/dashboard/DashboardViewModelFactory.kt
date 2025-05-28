package com.example.carlogger.ui.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carlogger.data.AppDatabase
import com.example.carlogger.data.repository.FirebaseTripRepository
import com.example.carlogger.data.repository.LocalTripRepository
import com.example.carlogger.data.repository.TripRepository

/**
 * Factory manual que inyecta TripRepository.
 * Cambia aquí entre LocalTripRepository(...) o FirebaseTripRepository().
 */
class DashboardViewModelFactory(
    private val app: Application,
    private val useRemote: Boolean = true
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            // Elige repositorio local o remoto:
            val repo: TripRepository = if (useRemote) {
                FirebaseTripRepository()
            } else {
                val dao = AppDatabase.getInstance(app).tripDao()
                LocalTripRepository(dao)
            }
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
