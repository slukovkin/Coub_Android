package com.all4drive.billcalc.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.all4drive.billcalc.data.room.dao.ElectricMeterDao
import com.all4drive.billcalc.data.room.dao.GasMeterDao
import com.all4drive.billcalc.data.room.dao.SettingsDao
import com.all4drive.billcalc.data.room.dao.WaterMeterDao
import com.all4drive.billcalc.data.room.entity.ElectricMeter
import com.all4drive.billcalc.data.room.entity.GasMeter
import com.all4drive.billcalc.data.room.entity.Settings
import com.all4drive.billcalc.data.room.entity.WaterMeter

@Database(
    version = 1, entities = [
        ElectricMeter::class,
        WaterMeter::class,
        GasMeter::class,
        Settings::class
    ]
)
abstract class Db : RoomDatabase() {

    abstract fun electric(): ElectricMeterDao
    abstract fun water(): WaterMeterDao
    abstract fun gas(): GasMeterDao
    abstract fun settings(): SettingsDao

    companion object {

        fun getDb(context: Context): Db {
            return Room.databaseBuilder(
                context,
                Db::class.java,
                "bill_calc.db"
            ).build()
        }
    }
}