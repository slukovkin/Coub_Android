package com.all4drive.billcalc.presentation.screens

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.all4drive.billcalc.R
import com.all4drive.billcalc.data.room.Db
import com.all4drive.billcalc.data.room.entity.GasMeter
import com.all4drive.billcalc.data.room.entity.Settings
import com.all4drive.billcalc.databinding.FragmentGasMeterBinding
import com.all4drive.billcalc.presentation.MainViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class GasCounterFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentGasMeterBinding
    private lateinit var oldMeter: GasMeter
    private lateinit var setting: Settings
    private lateinit var selectedDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGasMeterBinding.inflate(inflater, container, false)
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
                        val convertMonth = if (month < 10) {
                            "0${month + 1}"
                        } else {
                            (month + 1).toString()
                        }
                        selectedDate =
                            getString(R.string.selected_date, year, convertMonth, dayOfMonth)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        db.gas().getLastMeter().asLiveData().observe(viewLifecycleOwner) {
            oldMeter = it ?: DEFAULT_GAS_METER

            val date = oldMeter.createdAt
            binding.datePrevCounter.text = if (date.isNotEmpty()) {
                getString(R.string.date, date.slice(0..6))
            } else {
                ""
            }
            if (oldMeter.currentCounter == 0) {
                customDialog()
                binding.tvPrevCounter.text = oldMeter.currentCounter.toString()
            } else {
                binding.tvPrevCounter.text = oldMeter.currentCounter.toString()
            }
        }

        db.settings().getLastSettings().asLiveData().observe(viewLifecycleOwner) {
            setting = it ?: DEFAULT_SETTINGS
        }

        binding.tvPrevCounter.setOnClickListener {
            if (oldMeter.prevCounter == 0) {
                customDialog()
            }
        }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MenuFragment.newInstance()).commit()
        }

        binding.btnSaveGasMeter.setOnClickListener {
            val currentCounter = binding.edCurrentCounter.text.toString()
            if (currentCounter.isEmpty() || currentCounter.toInt() < oldMeter.currentCounter) {
                binding.edCurrentCounter.text.clear()
                binding.edCurrentCounter.error =
                    getString(R.string.the_current_readings_should_not_be_less_than_the_previous_ones)
                return@setOnClickListener
            }
            val meter = GasMeter(
                id = null,
                prevCounter = oldMeter.currentCounter,
                currentCounter = currentCounter.toInt(),
                currentFlow = (currentCounter.toInt() - oldMeter.currentCounter).toDouble(),
                payment = (currentCounter.toInt() - oldMeter.currentCounter).toDouble() * setting.gasPrice,
                createdAt = selectedDate
            )

            binding.edCurrentCounter.text.clear()
            viewModel.viewModelScope.launch {
                db.gas().insertValueMeter(meter)
            }
            Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_LONG)
                .show()

            viewModel.gasMeter.value = meter

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MenuFragment.newInstance()).commit()
        }
    }

    private fun customDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val cl = layoutInflater.inflate(R.layout.fragment_previous_counter_dialog, null)
        val counter = cl.findViewById<EditText>(R.id.edCounterValue)
        val btnSave = cl.findViewById<Button>(R.id.btnSave)
        val btnCancel = cl.findViewById<Button>(R.id.btnCancel)

        builder.setView(cl)
        val dialog = builder.create()
        btnSave.setOnClickListener {
            if (counter.text.isNotEmpty()) {
                oldMeter.currentCounter = counter.text.toString().toInt()
                binding.tvPrevCounter.text = counter.text.toString()
                builder.setCancelable(false)
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
            return@setOnClickListener
        }

        dialog.show()
    }

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        val DEFAULT_GAS_METER = GasMeter(
            id = null,
            prevCounter = 0,
            currentCounter = 0,
            currentFlow = 0.0,
            payment = 0.0,
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
        fun newInstance() = GasCounterFragment()
    }
}