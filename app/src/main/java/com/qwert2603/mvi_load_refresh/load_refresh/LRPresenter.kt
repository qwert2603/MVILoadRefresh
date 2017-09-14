package com.qwert2603.mvi_load_refresh.load_refresh

import android.support.annotation.CallSuper
import android.util.Log
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.Single

abstract class LRPresenter<I, M, V : LRView<M>> : MviBasePresenter<V, LRViewState<M>>() {

    protected abstract fun initialModelSingle(): Single<I>

    open protected val reloadIntent: Observable<Any> = Observable.never()

    protected val retryIntent: Observable<Any> = intent { it.retry() }
    protected val refreshIntent: Observable<Any> = intent { it.refresh() }

    protected fun loadRefreshPartialChanges(): Observable<LRPartialChange> = Observable.merge(
            Observable
                    .merge(
                            Observable.just(Any()),
                            reloadIntent,
                            retryIntent
                    )
                    .switchMap {
                        initialModelSingle()
                                .toObservable()
                                .map<LRPartialChange> { LRPartialChange.InitialModelLoaded(it) }
                                .onErrorReturn { LRPartialChange.LoadingError(it) }
                                .startWith(LRPartialChange.LoadingStarted)
                    },
            refreshIntent
                    .switchMap {
                        initialModelSingle()
                                .toObservable()
                                .map<LRPartialChange> { LRPartialChange.InitialModelLoaded(it) }
                                .onErrorReturn { LRPartialChange.RefreshError(it) }
                                .startWith(LRPartialChange.RefreshStarted)
                    }
    )

    abstract protected fun M.changeInitialModel(i: I): M

    @CallSuper
    open protected fun stateReducer(viewState: LRViewState<M>, change: PartialChange): LRViewState<M> {
        Log.d("AASSDD", "LRPresenter stateReducer $change")
        if (change !is LRPartialChange) throw Exception()
        return when (change) {
            LRPartialChange.LoadingStarted -> viewState.copy(loading = true, loadingError = null, canRefresh = false)
            is LRPartialChange.LoadingError -> viewState.copy(loading = false, loadingError = change.t)
            LRPartialChange.RefreshStarted -> viewState.copy(refreshing = true, refreshingError = null)
            is LRPartialChange.RefreshError -> viewState.copy(refreshing = false, refreshingError = change.t)
            is LRPartialChange.InitialModelLoaded<*> -> {
                @Suppress("UNCHECKED_CAST")
                viewState.copy(
                        loading = false,
                        loadingError = null,
                        model = viewState.model.changeInitialModel(change.i as I),
                        canRefresh = true,
                        refreshing = false
                )
            }
        }
    }
}