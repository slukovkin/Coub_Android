package com.all4drive.billcalc.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.all4drive.billcalc.data.room.entity.WaterMeter

@Dao
interface WaterMeterDao {
    @Insert
    fun insertValueMeter(meter: WaterMeter)

    @Query("SELECT * FROM water_meter ORDER BY id DESC LIMIT 1")
    fun getLastMeter(): WaterMeter

}