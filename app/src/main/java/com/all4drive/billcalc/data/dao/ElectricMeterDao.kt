package com.all4drive.billcalc.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.all4drive.billcalc.data.room.entity.ElectricMeter
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectricMeterDao {

    @Insert
    suspend fun insertValueMeter(meter: ElectricMeter)

    @Query("SELECT * FROM electric_meter ORDER BY id DESC LIMIT 1")
    fun getLastMeter(): Flow<ElectricMeter>

    @Query("DELETE FROM electric_meter")
    suspend fun deleteAll(): Int
}
