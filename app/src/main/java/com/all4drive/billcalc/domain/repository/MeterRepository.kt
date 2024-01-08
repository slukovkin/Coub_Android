package com.all4drive.billcalc.domain.repository

import com.all4drive.billcalc.domain.models.Meter

interface MeterRepository {
    fun addMeter(meter: Meter): Boolean
    fun getLastMeter(title: String): Meter
}