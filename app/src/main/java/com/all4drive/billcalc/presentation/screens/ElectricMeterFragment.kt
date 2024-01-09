package com.all4drive.billcalc.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.all4drive.billcalc.R
import com.all4drive.billcalc.data.room.Db
import com.all4drive.billcalc.data.room.entity.ElectricMeter
import com.all4drive.billcalc.databinding.FragmentElectricMeterBinding
import com.all4drive.billcalc.presentation.MainViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class ElectricMeterFragment : Fragment() {

    private lateinit var binding: FragmentElectricMeterBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var oldMeter: ElectricMeter = ElectricMeter(
        null,
        0,
        0,
        0.0,
        50.0,
        Calendar.getInstance().time.toString()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectricMeterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Db.getDb(requireContext())

        db.electric().getLastMeter().asLiveData().observe(viewLifecycleOwner) {
            oldMeter = it
            binding.tvPrevCounter.text = oldMeter.currentCounter.toString()
        }

        binding.btnSaveElectricMeter.setOnClickListener {
            val currentCounter = binding.edCurrentCounter.text.toString()
            if(currentCounter.isEmpty() || currentCounter.toInt() < oldMeter.currentCounter) {
                binding.edCurrentCounter.text.clear()
                binding.edCurrentCounter.error =
                    getString(R.string.the_current_readings_should_not_be_less_than_the_previous_ones)
                return@setOnClickListener
            }
            val meter = ElectricMeter(
                id = null,
                prevCounter = oldMeter.currentCounter,
                currentCounter = currentCounter.toInt(),
                currentFlow = (currentCounter.toInt() - oldMeter.currentCounter).toDouble(),
                payment = (currentCounter.toInt() - oldMeter.currentCounter).toDouble() * 2.64,
                createdAt = Calendar.getInstance().time.toString()
            )

            binding.edCurrentCounter.text.clear()
            viewModel.viewModelScope.launch {
                db.electric().insertValueMeter(meter)
            }

            viewModel.electricMeter.value = meter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ElectricMeterFragment()
    }
}