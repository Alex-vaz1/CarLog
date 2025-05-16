package com.example.carlogger.ui.dashboard

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carlogger.R
import com.example.carlogger.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewModel
    private lateinit var adapter: TripAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(DashboardViewModel::class.java)

        // Configurar RecyclerView
        adapter = TripAdapter()
        binding.recyclerTrips.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@DashboardFragment.adapter
        }

        // Observadores
        viewModel.trips.observe(viewLifecycleOwner) { trips ->
            adapter.trips = trips
        }

        viewModel.displayText.observe(viewLifecycleOwner) { text ->
            binding.chipToggle.text = text
            binding.chipToggle.isChecked = viewModel.isKmMode.value == true
        }

        viewModel.balance.observe(viewLifecycleOwner) { bal ->
            val colorRes = if (bal >= 0) R.color.saldo_positive else R.color.saldo_negative
            val color = ContextCompat.getColor(requireContext(), colorRes)
            binding.chipToggle.chipBackgroundColor = ColorStateList.valueOf(color)
        }

        // Toggle mode al hacer click en el Chip
        binding.chipToggle.setOnClickListener {
            viewModel.toggleMode()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
