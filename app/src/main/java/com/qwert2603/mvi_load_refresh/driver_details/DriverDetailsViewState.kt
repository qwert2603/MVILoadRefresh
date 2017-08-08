package com.qwert2603.mvi_load_refresh.driver_details

import com.qwert2603.mvi_load_refresh.model.Driver
import com.qwert2603.mvi_load_refresh.load_refresh.InitialModelHolder

data class DriverDetailsModel(
        val driver: Driver
) : InitialModelHolder<Driver> {
    override fun changeInitialModel(i: Driver) = copy(driver = i)
}