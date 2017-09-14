package com.qwert2603.mvi_load_refresh.load_refresh

sealed class LRPartialChange : PartialChange {
    object LoadingStarted : LRPartialChange()
    data class LoadingError(val t: Throwable) : LRPartialChange()
    object RefreshStarted : LRPartialChange()
    data class RefreshError(val t: Throwable) : LRPartialChange()
    data class InitialModelLoaded<out I>(val i: I) : LRPartialChange()
}