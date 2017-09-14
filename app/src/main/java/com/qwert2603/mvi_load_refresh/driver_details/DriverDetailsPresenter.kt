package com.qwert2603.mvi_load_refresh.driver_details

import com.qwert2603.mvi_load_refresh.load_refresh.LRPresenter
import com.qwert2603.mvi_load_refresh.load_refresh.LRViewState
import com.qwert2603.mvi_load_refresh.model.Driver
import com.qwert2603.mvi_load_refresh.model.DriversSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class DriverDetailsPresenter : LRPresenter<Long, Driver, DriverDetailsModel, DriverDetailsView>() {

    override fun initialModelSingle(key: Long): Single<Driver> = Single.just(DriversSource.DRIVERS)
            .map { it.single { it.id == key } }
            .delay(1, TimeUnit.SECONDS)
            .flatMap { if (System.currentTimeMillis() % 2 == 0L) Single.just(it) else Single.error(Exception()) }

    override fun DriverDetailsModel.changeInitialModel(i: Driver) = copy(driver = i)

    override fun bindIntents() {
        val observable = loadRefreshPartialChanges()
        val initialViewState = LRViewState(false, null, false, false, null, DriverDetailsModel(Driver(-1, "", "", -1)))
        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()), DriverDetailsView::render)
    }
}