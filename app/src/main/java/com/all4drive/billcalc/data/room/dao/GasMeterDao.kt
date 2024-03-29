package com.all4drive.billcalc.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.all4drive.billcalc.data.room.entity.GasMeter
import kotlinx.coroutines.flow.Flow

@Dao
interface GasMeterDao {
    @Insert
    suspend fun insertValueMeter(meter: GasMeter)

    @Query("SELECT * FROM gas_meter ORDER BY id DESC LIMIT 1")
    fun getLastMeter(): Flow<GasMeter>

    @Query("SELECT * FROM gas_meter WHERE created_at LIKE :month ORDER BY id DESC LIMIT 1")
    fun getMeterByMonthId(month: String): Flow<GasMeter>

    @Query("DELETE FROM gas_meter")
    suspend fun deleteAll(): Int

}