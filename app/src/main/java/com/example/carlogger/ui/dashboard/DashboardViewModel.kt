// app/src/main/java/com/example/carlogger/ui/dashboard/DashboardViewModel.kt
package com.example.carlogger.ui.dashboard

import androidx.lifecycle.*
import com.example.carlogger.data.model.Trip
import com.example.carlogger.data.repository.TripRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map

/**
 * ViewModel desacoplado de la capa de datos.
 * Recibe un TripRepository para poder usar Room o Firestore.
 */
class DashboardViewModel(
    private val repo: TripRepository
) : ViewModel() {

    companion object {
        // TODO: reemplazar por lógica real de precio/km si cambia
        private const val PRICE_PER_KM = 10.0
    }

    /**
     * LiveData de los últimos 10 trips, ordenados por fecha descendente
     */
    val trips: LiveData<List<Trip>> = repo.getTrips()
        .map { it.take(10) }
        .asLiveData()

    // Toggle: false = Saldo, true = km
    private val _isKmMode = MutableLiveData(false)
    val isKmMode: LiveData<Boolean> = _isKmMode

    fun toggleMode() {
        _isKmMode.value = !(_isKmMode.value ?: false)
    }

    // Balance actual (ejemplo): reemplazar con cálculo real
    private val _balance = MutableLiveData(1000.0)
    val balance: LiveData<Double> = _balance

    /**
     * Texto a mostrar en el Chip según el modo
     */
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

    /**
     * Inserta un nuevo trip delegando al repositorio
     */
    fun insertTrip(userId: String, last3Digits: Int) {
        viewModelScope.launch {
            repo.addTrip(userId, last3Digits)
        }
    }

    /**
     * Elimina un trip por su id delegando al repositorio
     */
    fun deleteTrip(tripId: Long) {
        viewModelScope.launch {
            repo.deleteTrip(tripId)
        }
    }
}
