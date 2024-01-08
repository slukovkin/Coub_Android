package com.all4drive.billcalc.data.room.repository

import com.all4drive.billcalc.domain.models.Meter
import com.all4drive.billcalc.domain.repository.MeterRepository

class ElectricMeterRepositoryImpl : MeterRepository {
    override fun addMeter(meter: Meter): Boolean {
        return true
    }

    override fun getLastMeter(): Meter {
        TODO()
    }
}