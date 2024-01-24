package com.all4drive.billcalc.data.room.repository

import com.all4drive.billcalc.domain.models.Counter
import com.all4drive.billcalc.domain.repository.CounterRepository

class WaterCounterRepositoryImpl: CounterRepository {
    override fun addMeter(counter: Counter): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLastMeter(): Counter {
        TODO("Not yet implemented")
    }
}