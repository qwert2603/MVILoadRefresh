package com.qwert2603.mvi_load_refresh.driver_list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.qwert2603.mvi_load_refresh.R
import com.qwert2603.mvi_load_refresh.driver_details.DriverDetailsFragment
import com.qwert2603.mvi_load_refresh.load_refresh.LRFragment
import com.qwert2603.mvi_load_refresh.load_refresh.LRViewState
import com.qwert2603.mvi_load_refresh.load_refresh.LoadRefreshPanel
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_drivers_list.*

class DriverListFragment : LRFragment<Any, DriverListModel, DriverListView, DriverListPresenter>(), DriverListView {

    override fun createPresenter() = DriverListPresenter()

    override fun key() = Any()

    override fun viewForSnackbar(): View = lr_panel

    override fun loadRefreshPanel(): LoadRefreshPanel = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_drivers_list, container, false)
    }

    private val driversAdapter = DriversAdapter()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drivers_RecyclerView.layoutManager = LinearLayoutManager(context)
        drivers_RecyclerView.adapter = driversAdapter
        driversAdapter.clickListener = { driverId ->
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, DriverDetailsFragment.newInstance(driverId))
                    .addToBackStack("bs")
                    .commit()
        }
    }

    override fun sortByName(): Observable<Boolean> = RxCompoundButton.checkedChanges(sortByName_CheckBox)

    override fun render(vs: LRViewState<DriverListModel>) {
        super.render(vs)

        if (!vs.loading && vs.loadingError == null) {
            driversAdapter.drivers = vs.model.showingList
        }

        sortByName_CheckBox.isChecked = vs.model.sortByName
    }
}