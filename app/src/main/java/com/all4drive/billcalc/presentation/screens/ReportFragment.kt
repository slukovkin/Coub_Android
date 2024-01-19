package com.all4drive.billcalc.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
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
    private val db by lazy { Db.getDb(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListWithArrayAdapter()

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MenuFragment.newInstance()).commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListWithArrayAdapter() {
        val data = mutableListOf(
            Month("01", getString(R.string.january)),
            Month("02", getString(R.string.february)),
            Month("03", getString(R.string.march)),
            Month("04", getString(R.string.april)),
            Month("05", getString(R.string.may)),
            Month("06", getString(R.string.june)),
            Month("07", getString(R.string.juli)),
            Month("08", getString(R.string.august)),
            Month("09", getString(R.string.september)),
            Month("10", getString(R.string.october)),
            Month("11", getString(R.string.november)),
            Month("12", getString(R.string.december)),
        )

        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            android.R.id.text1,
            data
        )

        binding.tvMonth.adapter = adapter
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val convertMonthToString = if (currentMonth < 10) {
            "0$currentMonth"
        } else {
            currentMonth.toString()
        }

        binding.tvMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = data[position]
                binding.tvCurrentDate.text = item.month
                val month = if (convertMonthToString == item.id) {
                    item.id
                } else {
                    item.id
                }
                val monthNumber = "%20%-${month}%"

                queryReport(db = db, month = monthNumber)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onNothingSelected(parent: AdapterView<*>?) {
//                val currentMonth = Calendar.getInstance().toInstant().toString().slice(0..5)
//                queryReport("%$currentMonth%")
                // TODO Если месяц отчета не выбран
            }
        }

    }

    private fun queryReport(db: Db, month: String) {

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
                    } else {
                        gotoOnEmptyNotFoundScreen()
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
                    } else {
                        gotoOnEmptyNotFoundScreen()
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
                    } else {
                        gotoOnEmptyNotFoundScreen()
                    }
                }
        }
    }

    private fun gotoOnEmptyNotFoundScreen() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, EmptyNotFound.newInstance()).commit()
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
