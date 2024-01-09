package com.all4drive.billcalc.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.all4drive.billcalc.data.room.entity.ElectricMeter

class MainViewModel: ViewModel() {
    val electricMeter: MutableLiveData<ElectricMeter> by lazy {
        MutableLiveData<ElectricMeter>()
    }
}