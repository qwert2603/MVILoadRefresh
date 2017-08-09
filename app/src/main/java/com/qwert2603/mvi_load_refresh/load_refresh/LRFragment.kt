package com.qwert2603.mvi_load_refresh.load_refresh

import android.support.annotation.CallSuper
import android.support.design.widget.Snackbar
import android.view.View
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.qwert2603.mvi_load_refresh.R
import io.reactivex.Observable

abstract class LRFragment<K, M : InitialModelHolder<*>, V : LRView<K, M>, P : MviBasePresenter<V, LRViewState<M>>>
    : MviFragment<V, P>(), LRView<K, M> {

    protected abstract val key: K

    protected abstract fun viewForSnackbar(): View
    protected abstract fun loadRefreshPanel(): LoadRefreshPanel

    override fun load(): Observable<K> = Observable.just(key)

    override fun retry(): Observable<K> = loadRefreshPanel().retryClicks().map { key }

    override fun refresh(): Observable<K> = loadRefreshPanel().refreshes().map { key }

    @CallSuper
    override fun render(vs: LRViewState<M>) {
        loadRefreshPanel().render(vs)
        if (vs.refreshingError != null) {
            Snackbar.make(viewForSnackbar(), R.string.refreshing_error_text, Snackbar.LENGTH_SHORT).show()
        }
    }
}