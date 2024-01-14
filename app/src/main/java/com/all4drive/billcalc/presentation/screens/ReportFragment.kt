package com.all4drive.billcalc.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.all4drive.billcalc.R
import com.all4drive.billcalc.data.room.Db
import com.all4drive.billcalc.databinding.FragmentReportBinding
import com.all4drive.billcalc.presentation.MainViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.roundToInt

class ReportFragment : Fragment() {
    private lateinit var adapter: ArrayAdapter<Month>
    private lateinit var binding: FragmentReportBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListWithArrayAdapter()

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MenuFragment.newInstance()).commit()
        }
    }

    private fun setupListWithArrayAdapter() {
        val data = mutableListOf(
            Month("01", "January 2024"),
            Month("02", "February 2024"),
            Month("03", "March 2024"),
            Month("04", "April 2024"),
            Month("05", "May 2024"),
            Month("06", "June 2024"),
            Month("07", "Juli 2024"),
            Month("08", "August 2024"),
            Month("09", "September 2024"),
            Month("10", "October 2024"),
            Month("11", "November 2024"),
            Month("12", "December 2024"),
        )

        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            android.R.id.text1,
            data
        )

        binding.tvMonth.adapter = adapter

        binding.tvMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = data[position]
                binding.tvCurrentDate.text = item.month
                val monthNumber = "%2024-${item.id}%"

                queryReport(monthNumber)
            }

            @SuppressLint("NewApi")
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val currentMonth = Calendar.getInstance().time.toInstant().toString().slice(0..6)
                queryReport("%$currentMonth%")
            }
        }

    }

    private fun queryReport(month: String) {
        val db = Db.getDb(requireContext())

        viewModel.viewModelScope.launch {
            db.electric().getMeterByMonthId(month).asLiveData()
                .observe(viewLifecycleOwner) {
                    if (it != null) {
                        with(binding) {
                            tvCurrentDate.text = (it.createdAt.slice(0..6))
                            prevEl.text = it.prevCounter.toString()
                            currEl.text = it.currentCounter.toString()
                            consumpEl.text = it.currentFlow.toString()
                            paymentEl.text = ((it.payment * 100).roundToInt() / 100.0).toString()
                        }
                    }
                }
            db.water().getMeterByMonthId(month).asLiveData()
                .observe(viewLifecycleOwner) {
                    if (it != null) {
                        with(binding) {
                            prevWater.text = it.prevCounter.toString()
                            currWater.text = it.currentCounter.toString()
                            consumpWater.text = it.currentFlow.toString()
                            paymentWater.text = ((it.payment * 100).roundToInt() / 100.0).toString()
                        }
                    }
                }
            db.gas().getMeterByMonthId(month).asLiveData()
                .observe(viewLifecycleOwner) {
                    if (it !== null) {
                        with(binding) {
                            prevGas.text = it.prevCounter.toString()
                            currGas.text = it.currentCounter.toString()
                            consumpGas.text = it.currentFlow.toString()
                            paymentGas.text = ((it.payment * 100).roundToInt() / 100.0).toString()
                        }
                    }
                }
        }
    }

    class Month(
        val id: String,
        val month: String
    ) {
        override fun toString(): String {
            return month
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ReportFragment()
    }
}
