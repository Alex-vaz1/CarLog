package com.example.carlogger.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.carlogger.data.model.Trip
import com.example.carlogger.data.repository.FirebaseTripRepository
import kotlinx.coroutines.flow.map

class HomeViewModel : ViewModel() {

    private val repo = FirebaseTripRepository()

    // Trae todos los viajes ordenados por fecha descendente
    val allTrips: LiveData<List<Trip>> = repo.getTrips()//getALlTrips para sin limites
        .map { trips -> trips.sortedByDescending { it.date } }
        .asLiveData()
}
