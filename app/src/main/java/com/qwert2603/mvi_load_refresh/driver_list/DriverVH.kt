package com.qwert2603.mvi_load_refresh.driver_list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.qwert2603.mvi_load_refresh.R
import com.qwert2603.mvi_load_refresh.model.Driver
import com.qwert2603.mvi_load_refresh.util.inflate
import kotlinx.android.synthetic.main.item_driver.view.*

class DriverVH(parent: ViewGroup, clickListener: ((Long) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_driver)) {
    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                clickListener?.invoke(itemId)
            }
        }
    }

    fun render(driver: Driver) = with(itemView) {
        name.text = driver.name
        team.text = driver.team
        birthYear.text = driver.birthYear.toString()
    }
}