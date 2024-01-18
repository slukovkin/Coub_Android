package com.all4drive.billcalc.domain.models

data class Setting(
    val electricPrice: Double,
    val waterPrice: Double,
    val gasPrice: Double,
    val createdAt: String,
    val id: Int? = null
)