package com.qwert2603.mvi_load_refresh.driver_list

import com.qwert2603.mvi_load_refresh.load_refresh.LRView
import io.reactivex.Observable

interface DriverListView : LRView<Any, DriverListModel> {
    fun sortByName(): Observable<Boolean>
}