package com.all4drive.billcalc.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.all4drive.billcalc.data.room.entity.ElectricMeter
import com.all4drive.billcalc.data.room.entity.GasMeter
import com.all4drive.billcalc.data.room.entity.Settings
import com.all4drive.billcalc.data.room.entity.WaterMeter
import java.util.Calendar

class MainViewModel: ViewModel() {
    val electricMeter: MutableLiveData<ElectricMeter> by lazy {
        MutableLiveData<ElectricMeter>()
    }

    val waterMeter: MutableLiveData<WaterMeter> by lazy {
        MutableLiveData<WaterMeter>()
    }

    val gasMeter: MutableLiveData<GasMeter> by lazy {
        MutableLiveData<GasMeter>()
    }

    val settings: MutableLiveData<Settings> by lazy {
        MutableLiveData<Settings>()
    }
}