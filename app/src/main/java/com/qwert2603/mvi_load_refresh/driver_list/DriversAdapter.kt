package com.qwert2603.mvi_load_refresh.driver_list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.qwert2603.mvi_load_refresh.model.Driver

class DriversAdapter : RecyclerView.Adapter<DriverVH>() {
    var drivers: List<Driver> = emptyList()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = old.size
                override fun getNewListSize() = field.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition].id == field[newItemPosition].id
                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == field[newItemPosition]
            }).dispatchUpdatesTo(this)
        }

    var clickListener: ((Long) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DriverVH(parent, clickListener)
    override fun onBindViewHolder(holder: DriverVH, position: Int) = holder.render(drivers[position])
    override fun getItemCount() = drivers.size
    override fun getItemId(position: Int) = drivers[position].id
}