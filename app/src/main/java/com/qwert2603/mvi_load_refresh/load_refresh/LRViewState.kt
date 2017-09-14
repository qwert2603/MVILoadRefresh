package com.qwert2603.mvi_load_refresh.load_refresh

data class LRViewState<out M>(
        val loading: Boolean,
        val loadingError: Throwable?,
        val canRefresh: Boolean,
        val refreshing: Boolean,
        val refreshingError: Throwable?,
        val model: M
)