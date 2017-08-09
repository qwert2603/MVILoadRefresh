package com.qwert2603.mvi_load_refresh.load_refresh

import io.reactivex.Observable

interface LoadRefreshPanel {
    fun retryClicks(): Observable<Any>
    fun refreshes(): Observable<Any>

    fun render(vs: LRViewState<*>)
}