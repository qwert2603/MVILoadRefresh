package com.qwert2603.mvi_load_refresh.load_refresh

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface LRView<in M> : MvpView {
    fun retry(): Observable<Any>
    fun refresh(): Observable<Any>

    fun render(vs: LRViewState<M>)
}