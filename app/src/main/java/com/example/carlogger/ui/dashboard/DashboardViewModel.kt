package com.example.carlogger.ui.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.example.carlogger.data.AppDatabase
import com.example.carlogger.data.model.Trip
import kotlinx.coroutines.flow.map

class DashboardViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        // TODO: reemplazar por lógica real de precio/km
        private const val PRICE_PER_KM = 10.0
    }

    private val tripDao = AppDatabase.getInstance(app).tripDao()

    // Lista de los últimos 10 trips
    val trips: LiveData<List<Trip>> = tripDao
        .getAllTrips()
        .map { it.take(10) }
        .asLiveData()

    // Estado toggle: false = mostrar saldo, true = mostrar km
    private val _isKmMode = MutableLiveData(false)
    val isKmMode: LiveData<Boolean> = _isKmMode

    fun toggleMode() {
        _isKmMode.value = !(_isKmMode.value ?: false)
    }

    // TODO: reemplazar por cálculo real desde FuelEntryDao
    private val _balance = MutableLiveData(1000.0)
    val balance: LiveData<Double> = _balance

    // Texto a mostrar en el Chip (Saldo o km)
    val displayText: LiveData<String> = MediatorLiveData<String>().apply {
        fun update() {
            val bal = _balance.value ?: 0.0
            value = if (_isKmMode.value == true) {
                val km = (bal / PRICE_PER_KM).toInt()
                "$km km"
            } else {
                "Saldo: \$${bal.toInt()}"
            }
        }
        addSource(_isKmMode) { update() }
        addSource(_balance) { update() }
    }
}
