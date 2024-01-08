package com.all4drive.billcalc.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.all4drive.billcalc.data.room.entity.Settings

@Dao
interface SettingsDao {

    @Insert
    fun insertValueSettings(settings: Settings)

    @Query("SELECT * FROM settings ORDER BY id DESC LIMIT 1")
    fun getLastSettings(): Settings

}