package com.all4drive.billcalc.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [])
abstract class Db : RoomDatabase() {

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