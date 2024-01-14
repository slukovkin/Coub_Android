package com.all4drive.billcalc.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.all4drive.billcalc.data.room.entity.WaterMeter
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterMeterDao {
    @Insert
    suspend fun insertValueMeter(meter: WaterMeter)

    @Query("SELECT * FROM water_meter ORDER BY id DESC LIMIT 1")
    fun getLastMeter(): Flow<WaterMeter>

    @Query("SELECT * FROM water_meter WHERE created_at LIKE :month ORDER BY id DESC LIMIT 1")
    fun getMeterByMonthId(month: String) : Flow<WaterMeter>

    @Query("DELETE FROM water_meter")
    suspend fun deleteAll(): Int

}