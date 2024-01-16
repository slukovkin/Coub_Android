package com.all4drive.billcalc.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.all4drive.billcalc.R
import com.all4drive.billcalc.databinding.FragmentEmptyNotFoundBinding

class EmptyNotFound : Fragment() {

    private lateinit var binding: FragmentEmptyNotFoundBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmptyNotFoundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MenuFragment.newInstance()).commit()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = EmptyNotFound()
    }
}