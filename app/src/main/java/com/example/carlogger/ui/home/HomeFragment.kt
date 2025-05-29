package com.example.carlogger.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carlogger.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: TripAdapterForHome

    private val pickFile = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { importFromExcel(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, HomeViewModelFactory()).get(HomeViewModel::class.java)

        adapter = TripAdapterForHome()
        binding.recyclerHomeTrips.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHomeTrips.adapter = adapter

        viewModel.allTrips.observe(viewLifecycleOwner) {
            adapter.trips = it
        }

        binding.btnImportExcel.setOnClickListener {
            pickFile.launch(
                arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            )
        }
    }

    private fun importFromExcel(uri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val stream = requireContext().contentResolver.openInputStream(uri)!!
                val workbook = XSSFWorkbook(stream)
                val sheet = workbook.getSheetAt(0)

                val fmt = DateTimeFormatter.ofPattern("d-M-yy")
                val zone = ZoneId.systemDefault()
                val col = Firebase.firestore.collection("trips")

                for (row in sheet.drop(1)) {
                    try {
                        val piloto = row.getCell(0)?.stringCellValue?.trim() ?: continue
                        val last3 = row.getCell(1)?.numericCellValue?.toInt() ?: continue
                        val odometer = row.getCell(2)?.numericCellValue?.toInt() ?: continue
                        val distance = row.getCell(3)?.numericCellValue?.toInt() ?: continue

                        val price = when (val cell = row.getCell(4)) {
                            null -> continue
                            else -> when (cell.cellType) {
                                CellType.NUMERIC -> cell.numericCellValue
                                CellType.STRING -> cell.stringCellValue.replace(',', '.').toDoubleOrNull() ?: continue
                                else -> continue
                            }
                        }

                        val dateStr: String = when (val cell = row.getCell(5)) {
                            null -> continue
                            else -> when (cell.cellType) {
                                CellType.STRING -> cell.stringCellValue.trim()
                                CellType.NUMERIC -> cell.dateCellValue.toInstant().atZone(zone).toLocalDate().format(fmt)
                                else -> continue
                            }
                        }

                        val localDate = LocalDate.parse(dateStr, fmt)
                        val instant = localDate.atStartOfDay(zone).toInstant()

                        val data = mapOf(
                            "userId" to piloto,
                            "odometer" to odometer,
                            "distanceTraveled" to distance,
                            "price" to price,
                            "date" to Date.from(instant)
                        )

                        col.add(data).await()
                    } catch (rowEx: Exception) {
                        rowEx.printStackTrace()
                    }
                }

                workbook.close()
                stream.close()

                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Importación completada", Toast.LENGTH_LONG).show()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error al importar el archivo", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
