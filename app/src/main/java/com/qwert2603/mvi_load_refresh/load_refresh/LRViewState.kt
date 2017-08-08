package com.qwert2603.mvi_load_refresh.load_refresh

interface InitialModelHolder<in I> {
    fun changeInitialModel(i: I): InitialModelHolder<I>
}

data class LRViewState<out M : InitialModelHolder<*>>(
        val loading: Boolean,
        val loadingError: Throwable?,
        val canRefresh: Boolean,
        val refreshing: Boolean,
        val refreshingError: Throwable?,
        val model: M
)

sealed class LRPartialChange : PartialChange {
    object LoadingStarted : LRPartialChange()
    data class LoadingError(val t: Throwable) : LRPartialChange()
    object RefreshStarted : LRPartialChange()
    data class RefreshError(val t: Throwable) : LRPartialChange()
    data class InitialModelLoaded<out I>(val i: I) : LRPartialChange()
}