package com.all4drive.billcalc.domain.repository

import com.all4drive.billcalc.domain.models.Setting

interface SettingRepository {
    fun addSetting(setting: Setting): Boolean
    fun getLastSetting(): Setting
}