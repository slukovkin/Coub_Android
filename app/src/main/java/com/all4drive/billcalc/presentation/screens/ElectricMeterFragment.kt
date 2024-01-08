package com.all4drive.billcalc.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.all4drive.billcalc.databinding.FragmentElectricMeterBinding

class ElectricMeterFragment : Fragment() {

    private lateinit var binding: FragmentElectricMeterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectricMeterBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ElectricMeterFragment()
    }
}