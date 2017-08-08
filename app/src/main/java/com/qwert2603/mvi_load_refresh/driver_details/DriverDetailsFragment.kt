package com.qwert2603.mvi_load_refresh.driver_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.qwert2603.mvi_load_refresh.R
import com.qwert2603.mvi_load_refresh.load_refresh.LRFragment
import com.qwert2603.mvi_load_refresh.load_refresh.LRViewState
import com.qwert2603.mvi_load_refresh.load_refresh.LoadRefreshPanel
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_driver_details.*

class DriverDetailsFragment : LRFragment<Long, DriverDetailsModel, DriverDetailsView, DriverDetailsPresenter>(), DriverDetailsView {

    companion object {
        private val driverIdKey = "driverId"

        fun newInstance(driverId: Long) = DriverDetailsFragment()
                .also {
                    val bundle = Bundle()
                    bundle.putLong(driverIdKey, driverId)
                    it.arguments = bundle
                }
    }

    override fun key(): Long = arguments.getLong(driverIdKey)

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = RxView.clicks(retry_Button)

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {
            retry_panel.visibility = if (vs.loadingError != null) View.VISIBLE else View.GONE
            if (vs.loading) {
                name.text = "...."
                team.text = "...."
                birthYear.text = "...."
            }
        }
    }

    override fun createPresenter() = DriverDetailsPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_driver_details, container, false)
    }

    override fun render(vs: LRViewState<DriverDetailsModel>) {
        super.render(vs)

        if (!vs.loading && vs.loadingError == null) {
            name.text = vs.model.driver.name
            team.text = vs.model.driver.team
            birthYear.text = vs.model.driver.birthYear.toString()
        }
    }
}