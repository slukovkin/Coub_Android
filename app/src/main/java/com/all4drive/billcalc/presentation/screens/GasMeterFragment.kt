package com.all4drive.billcalc.presentation.screens

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
import com.all4drive.billcalc.data.room.entity.GasMeter
import com.all4drive.billcalc.data.room.entity.Settings
import com.all4drive.billcalc.databinding.FragmentGasMeterBinding
import com.all4drive.billcalc.presentation.MainViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class GasMeterFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentGasMeterBinding
    private lateinit var oldMeter: GasMeter
    private lateinit var setting: Settings

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
            // showDataDialog()
            binding.edCurrentCounter.isFocusableInTouchMode = true
        }

        db.gas().getLastMeter().asLiveData().observe(viewLifecycleOwner) {
            oldMeter = it ?: DEFAULT_GAS_METER

            val date = oldMeter.createdAt
            binding.datePrevCounter.text = getString(R.string.date, date.slice(0..9))
            binding.tvPrevCounter.text = oldMeter.currentCounter.toString()
        }

        db.settings().getLastSettings().asLiveData().observe(viewLifecycleOwner) {
            setting = it ?: DEFAULT_SETTINGS
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
                createdAt = Calendar.getInstance().time.toInstant().toString()
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

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        val DEFAULT_GAS_METER = GasMeter(
            id = null,
            prevCounter = 0,
            currentCounter = 0,
            currentFlow = 0.0,
            payment = 0.0,
            createdAt = Calendar.getInstance().time.toInstant().toString()
        )

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        val DEFAULT_SETTINGS = Settings(
            id = null,
            electricPrice = 0.0,
            waterPrice = 0.0,
            gasPrice = 0.0,
            createdAt = Calendar.getInstance().time.toInstant().toString()
        )

        @JvmStatic
        fun newInstance() = GasMeterFragment()
    }
}