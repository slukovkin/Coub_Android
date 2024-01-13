package com.all4drive.billcalc.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.all4drive.billcalc.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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