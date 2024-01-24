package com.all4drive.billcalc.domain.repository

import com.all4drive.billcalc.domain.models.Counter

interface CounterRepository {
    fun addMeter(counter: Counter): Boolean
    fun getLastMeter(): Counter
}