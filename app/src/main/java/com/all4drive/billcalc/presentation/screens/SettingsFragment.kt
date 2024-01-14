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
import com.all4drive.billcalc.data.room.entity.Settings
import com.all4drive.billcalc.databinding.FragmentSettingsBinding
import com.all4drive.billcalc.presentation.MainViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var oldSettings: Settings

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Db.getDb(requireContext())

        binding.btnClearDb.setOnClickListener {
            viewModel.viewModelScope.launch {
                db.gas().deleteAll()
                db.water().deleteAll()
                db.electric().deleteAll()
                db.settings().deleteAll()
            }
        }

        db.settings().getLastSettings().asLiveData().observe(viewLifecycleOwner) {
            oldSettings = it ?: DEFAULT_SETTINGS

            binding.edElectricPrice.hint = oldSettings.electricPrice.toString()
            binding.edWaterPrice.hint = oldSettings.waterPrice.toString()
            binding.edGasPrice.hint = oldSettings.gasPrice.toString()
        }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MenuFragment.newInstance()).commit()
        }

        binding.btnSaveSettings.setOnClickListener {
            if (
                binding.edElectricPrice.text.toString().isEmpty() ||
                binding.edWaterPrice.text.toString().isEmpty() ||
                binding.edGasPrice.text.toString().isEmpty()
            ) {
                Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            } else {
                val elPrice = binding.edElectricPrice.text.toString().toDouble()
                val wtPrice = binding.edWaterPrice.text.toString().toDouble()
                val gsPrice = binding.edGasPrice.text.toString().toDouble()

                val setting = Settings(
                    id = null,
                    electricPrice = elPrice,
                    waterPrice = wtPrice,
                    gasPrice = gsPrice,
                    createdAt = Calendar.getInstance().time.toInstant().toString()
                )
                viewModel.viewModelScope.launch {
                    db.settings().insertValueSettings(setting)
                }

                with(binding) {
                    edElectricPrice.text.clear()
                    edWaterPrice.text.clear()
                    edGasPrice.text.clear()
                }
                Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_LONG)
                    .show()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, MenuFragment.newInstance()).commit()
            }
        }

    }

    companion object {

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
        fun newInstance() = SettingsFragment()
    }
}