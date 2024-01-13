package com.all4drive.billcalc.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.all4drive.billcalc.data.room.Db
import com.all4drive.billcalc.databinding.FragmentReportBinding
import com.all4drive.billcalc.presentation.MainViewModel
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Db.getDb(requireContext())

        viewModel.viewModelScope.launch {
            db.electric().getLastMeter().asLiveData().observe(viewLifecycleOwner) {
                with(binding) {
                    tvCurrentDate.text = (it.createdAt.slice(3..7)).toString() +
                            (it.createdAt.slice(29..33)).toString()
                    prevEl.text = it.prevCounter.toString()
                    currEl.text = it.currentCounter.toString()
                    consumpEl.text = it.currentFlow.toString()
                    paymentEl.text = ((it.payment * 100).roundToInt() / 100.0).toString()
                }

            }
            db.water().getLastMeter().asLiveData().observe(viewLifecycleOwner) {
                with(binding) {
                    prevWater.text = it.prevCounter.toString()
                    currWater.text = it.currentCounter.toString()
                    consumpWater.text = it.currentFlow.toString()
                    paymentWater.text = ((it.payment * 100).roundToInt() / 100.0).toString()
                }
            }
            db.gas().getLastMeter().asLiveData().observe(viewLifecycleOwner) {
                with(binding) {
                    prevGas.text = it.prevCounter.toString()
                    currGas.text = it.currentCounter.toString()
                    consumpGas.text = it.currentFlow.toString()
                    paymentGas.text = ((it.payment * 100).roundToInt() / 100.0).toString()

                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ReportFragment()
    }
}