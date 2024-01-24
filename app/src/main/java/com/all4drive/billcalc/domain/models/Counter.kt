package com.all4drive.billcalc.domain.models

data class Counter(
    val prevCounter: Int,
    val currentCounter: Int,
    val currentFlow: Double,
    val payment: Double,
    val createdAt: String,
    val id: Int? = null
)