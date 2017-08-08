package com.qwert2603.mvi_load_refresh.load_refresh

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface LRView<K, in M : InitialModelHolder<*>> : MvpView {
    fun load(): Observable<K>
    fun retry(): Observable<K>
    fun refresh(): Observable<K>

    fun render(vs: LRViewState<M>)
}