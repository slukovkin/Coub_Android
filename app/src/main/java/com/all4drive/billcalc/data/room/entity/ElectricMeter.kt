package com.all4drive.billcalc.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "electric_meter")
data class ElectricMeter(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "prev_counter") val prevCounter: Int,
    @ColumnInfo(name = "curr_counter") val currentCounter: Int,
    @ColumnInfo(name = "curr_flow") val currentFlow: Double,
    @ColumnInfo(name = "payment") val payment: Double,
    @ColumnInfo(name = "created_at") val createdAt: String
)
