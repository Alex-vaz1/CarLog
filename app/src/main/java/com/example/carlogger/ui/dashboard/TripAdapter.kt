package com.example.carlogger.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carlogger.data.model.Trip
import com.example.carlogger.databinding.ItemTripBinding
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TripAdapter : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    var trips: List<Trip> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        .withZone(ZoneId.systemDefault())

    inner class TripViewHolder(private val binding: ItemTripBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: Trip) {
            binding.textUserId.text = trip.userId
            binding.textDistance.text = "${trip.distanceTraveled} km"
            binding.textPrice.text = "$${trip.price.toInt()}"
            binding.textDate.text = dateFormatter.format(trip.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(trips[position])
    }

    override fun getItemCount(): Int = trips.size
}
