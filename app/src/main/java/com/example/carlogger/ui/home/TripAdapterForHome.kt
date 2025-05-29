package com.example.carlogger.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carlogger.data.model.Trip
import com.example.carlogger.databinding.ItemTripHomeBinding
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TripAdapterForHome : RecyclerView.Adapter<TripAdapterForHome.ViewHolder>() {

    var trips: List<Trip> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        .withZone(ZoneId.systemDefault())

    inner class ViewHolder(private val binding: ItemTripHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: Trip) = with(binding) {
            textUserId.text = trip.userId
            textOdometer.text = trip.odometer.toString()
            textDistance.text = "${trip.distanceTraveled} km"
            textPrice.text = "$${trip.price.toInt()}"
            textDate.text = formatter.format(trip.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTripHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trips[position])
    }

    override fun getItemCount(): Int = trips.size
}
