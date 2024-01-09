package com.all4drive.billcalc.domain.models

class Meter(
    val id: Int?,
    val prevCounter: Int,
    val currentCounter: Int,
    val currentFlow: Double,
    val payment: Double,
    val createdAt: String
)