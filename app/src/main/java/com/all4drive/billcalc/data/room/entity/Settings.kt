package com.all4drive.billcalc.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "electric_price") val electricPrice: Double,
    @ColumnInfo(name = "water_price") val waterPrice: Double,
    @ColumnInfo(name = "gas_price") val gasPrice: Double,
    @ColumnInfo(name = "created_at") val createdAt: String
)
