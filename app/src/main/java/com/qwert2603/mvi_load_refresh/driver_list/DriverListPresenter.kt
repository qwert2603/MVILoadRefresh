package com.qwert2603.mvi_load_refresh.driver_list

import com.qwert2603.mvi_load_refresh.load_refresh.LRPresenter
import com.qwert2603.mvi_load_refresh.load_refresh.LRViewState
import com.qwert2603.mvi_load_refresh.load_refresh.PartialChange
import com.qwert2603.mvi_load_refresh.model.Driver
import com.qwert2603.mvi_load_refresh.model.DriversSource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class DriverListPresenter : LRPresenter<Any, List<Driver>, DriverListModel, DriverListView>() {

    override fun initialModelSingle(key: Any): Single<List<Driver>> = Single.just(DriversSource.DRIVERS)
            .delay(1, TimeUnit.SECONDS)
            .flatMap { if (System.currentTimeMillis() % 2 == 0L) Single.just(it) else Single.error(Exception()) }

    override fun bindIntents() {
        val observable = Observable.merge(
                loadRefreshPartialChanges(),
                intent { it.sortByName() }
                        .map<PartialChange> { DriverListPartialChanges.SortChange(it) }
        )
        val initialViewState = LRViewState(false, null, false, false, null, DriverListModel(emptyList(), false))
        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()), DriverListView::render)
    }

    override fun stateReducer(viewState: LRViewState<DriverListModel>, change: PartialChange): LRViewState<DriverListModel> {
        if (change !is DriverListPartialChanges) return super.stateReducer(viewState, change)
        return when (change) {
            is DriverListPartialChanges.SortChange -> viewState.copy(model = viewState.model.copy(sortByName = change.sortByName))
        }
    }

}