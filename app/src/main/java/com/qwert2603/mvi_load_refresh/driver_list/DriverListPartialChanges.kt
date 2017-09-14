package com.qwert2603.mvi_load_refresh.driver_list

import com.qwert2603.mvi_load_refresh.load_refresh.PartialChange

sealed class DriverListPartialChanges : PartialChange {
    data class SortChange(val sortByName: Boolean) : DriverListPartialChanges()
}