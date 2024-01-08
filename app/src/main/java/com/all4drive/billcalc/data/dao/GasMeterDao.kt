package com.all4drive.billcalc.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.all4drive.billcalc.data.room.entity.GasMeter

@Dao
interface GasMeterDao {
    @Insert
    fun insertValueMeter(meter: GasMeter)

    @Query("SELECT * FROM gas_meter ORDER BY id DESC LIMIT 1")
    fun getLastMeter(): GasMeter

}