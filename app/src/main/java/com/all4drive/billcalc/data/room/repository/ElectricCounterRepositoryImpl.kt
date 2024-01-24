package com.all4drive.billcalc.data.room.repository

import com.all4drive.billcalc.domain.models.Counter
import com.all4drive.billcalc.domain.repository.CounterRepository

class ElectricCounterRepositoryImpl : CounterRepository {
    override fun addMeter(counter: Counter): Boolean {
        return true
    }

    override fun getLastMeter(): Counter {
        TODO()
    }
}