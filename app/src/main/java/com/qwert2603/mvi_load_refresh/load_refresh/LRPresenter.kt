package com.qwert2603.mvi_load_refresh.load_refresh

import android.support.annotation.CallSuper
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

abstract class LRPresenter<K, I, M : InitialModelHolder<I>, V : LRView<K, M>> : MviBasePresenter<V, LRViewState<M>>() {

    protected abstract fun initialModelSingle(key: K): Single<I>

    open protected val reloadIntent: Observable<Any> = Observable.never()

    protected val loadIntent: Observable<K> = intent { it.load() }
    protected val retryIntent: Observable<K> = intent { it.retry() }
    protected val refreshIntent: Observable<K> = intent { it.refresh() }

    protected fun loadRefreshPartialChanges(): Observable<LRPartialChange> = Observable.merge(
            Observable
                    .merge(
                            Observable.combineLatest(
                                    loadIntent,
                                    reloadIntent.startWith(Any()),
                                    BiFunction { k, _ -> k }
                            ),
                            retryIntent
                    )
                    .switchMap {
                        initialModelSingle(it)
                                .toObservable()
                                .map<LRPartialChange> { LRPartialChange.InitialModelLoaded(it) }
                                .onErrorReturn { LRPartialChange.LoadingError(it) }
                                .startWith(LRPartialChange.LoadingStarted)
                    },
            refreshIntent
                    .switchMap {
                        initialModelSingle(it)
                                .toObservable()
                                .map<LRPartialChange> { LRPartialChange.InitialModelLoaded(it) }
                                .onErrorReturn { LRPartialChange.RefreshError(it) }
                                .startWith(LRPartialChange.RefreshStarted)
                    }
    )

    @CallSuper
    open protected fun stateReducer(viewState: LRViewState<M>, change: PartialChange): LRViewState<M> {
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
                        model = viewState.model.changeInitialModel(change.i as I) as M,
                        canRefresh = true,
                        refreshing = false
                )
            }
        }
    }
}