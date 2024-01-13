package com.all4drive.billcalc.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import com.all4drive.billcalc.R
import com.all4drive.billcalc.data.room.Db
import com.all4drive.billcalc.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Db.getDb(requireContext())

        db.settings().getLastSettings().asLiveData().observe(viewLifecycleOwner) {
            if (it == null) {
                binding.info.text = "Please enter current rates"
                with(binding) {
                    btnReportScreen.isEnabled = false
                    btnElectricScreen.isEnabled = false
                    btnWaterScreen.isEnabled = false
                    btnGasScreen.isEnabled = false
                }
            }
        }

        binding.btnElectricScreen.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, ElectricMeterFragment.newInstance()).commit()
        }

        binding.btnWaterScreen.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, WaterMeterFragment.newInstance()).commit()
        }

        binding.btnGasScreen.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, GasMeterFragment.newInstance()).commit()
        }

        binding.btnReportScreen.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, ReportFragment.newInstance()).commit()
        }

        binding.btnSettingScreen.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, SettingsFragment.newInstance()).commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}