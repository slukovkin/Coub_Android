package com.all4drive.billcalc.presentation.screens

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.all4drive.billcalc.R
import com.all4drive.billcalc.data.room.Db
import com.all4drive.billcalc.data.room.entity.Settings
import com.all4drive.billcalc.data.room.entity.WaterMeter
import com.all4drive.billcalc.databinding.FragmentWaterMeterBinding
import com.all4drive.billcalc.presentation.MainViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class WaterMeterFragment : Fragment() {

    private lateinit var binding: FragmentWaterMeterBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var oldMeter: WaterMeter
    private lateinit var setting: Settings
    private lateinit var selectedDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaterMeterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Db.getDb(requireContext())

        binding.edCurrentCounter.setOnClickListener {
            binding.edCurrentCounter.setOnClickListener {
                val calendar = Calendar.getInstance()
                binding.edCurrentCounter.isFocusableInTouchMode = true

                DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        selectedDate =
                            getString(R.string.selected_date, year, month + 1, dayOfMonth)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        db.water().getLastMeter().asLiveData().observe(viewLifecycleOwner) {
            oldMeter = it ?: DEFAULT_WATER_METER

            val date = oldMeter.createdAt
            binding.datePrevCounter.text = if (date.isNotEmpty()) {
                getString(R.string.date, date.slice(0..5))
            } else {
                ""
            }
            binding.tvPrevCounter.text = oldMeter.currentCounter.toString()
        }

        db.settings().getLastSettings().asLiveData().observe(viewLifecycleOwner) {
            setting = it ?: DEFAULT_SETTINGS
        }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MenuFragment.newInstance()).commit()
        }

        binding.btnSaveWaterMeter.setOnClickListener {
            val currentCounter = binding.edCurrentCounter.text.toString()
            if (currentCounter.isEmpty() || currentCounter.toInt() < oldMeter.currentCounter) {
                binding.edCurrentCounter.text.clear()
                binding.edCurrentCounter.error =
                    getString(R.string.the_current_readings_should_not_be_less_than_the_previous_ones)
                return@setOnClickListener
            }
            val meter = WaterMeter(
                id = null,
                prevCounter = oldMeter.currentCounter,
                currentCounter = currentCounter.toInt(),
                currentFlow = (currentCounter.toInt() - oldMeter.currentCounter).toDouble(),
                payment = (currentCounter.toInt() - oldMeter.currentCounter).toDouble() * setting.waterPrice,
                createdAt = selectedDate
            )

            binding.edCurrentCounter.text.clear()
            viewModel.viewModelScope.launch {
                db.water().insertValueMeter(meter)
            }

            Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_LONG)
                .show()

            viewModel.waterMeter.value = meter

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MenuFragment.newInstance()).commit()
        }
    }

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        val DEFAULT_WATER_METER = WaterMeter(
            id = null,
            prevCounter = 0,
            currentCounter = 0,
            currentFlow = .0,
            payment = .0,
            createdAt = ""
        )

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        val DEFAULT_SETTINGS = Settings(
            id = null,
            electricPrice = 0.0,
            waterPrice = 0.0,
            gasPrice = 0.0,
            createdAt = ""
        )

        @JvmStatic
        fun newInstance() = WaterMeterFragment()
    }
}