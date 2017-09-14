package com.qwert2603.mvi_load_refresh.driver_list

import com.qwert2603.mvi_load_refresh.load_refresh.PartialChange
import com.qwert2603.mvi_load_refresh.model.Driver

data class DriverListModel(
        val drivers: List<Driver>,
        val sortByName: Boolean
) {
    val showingList by lazy { if (sortByName) drivers.sortedBy { it.name } else drivers }
}

sealed class DriverListPartialChanges : PartialChange {
    data class SortChange(val sortByName: Boolean) : DriverListPartialChanges()
}