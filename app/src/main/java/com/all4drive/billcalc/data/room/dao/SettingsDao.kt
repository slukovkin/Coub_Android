package com.all4drive.billcalc.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.all4drive.billcalc.data.room.entity.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert
    suspend fun insertValueSettings(settings: Settings)

    @Query("SELECT * FROM settings ORDER BY id DESC LIMIT 1")
    fun getLastSettings(): Flow<Settings>

    @Query("DELETE FROM settings")
    suspend fun deleteAll(): Int
}