package com.qwert2603.mvi_load_refresh.load_refresh

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import com.qwert2603.mvi_load_refresh.R
import com.qwert2603.mvi_load_refresh.util.showIfNotYet
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_load_refresh.view.*

/**
 * Implementation of [LoadRefreshPanel] that contains of:
 * - [android.widget.ViewAnimator] ([LR_ViewAnimator]) to switch between layers of:
 *      1. loading indicator;
 *      2. error(retry) panel;
 *      3. model.
 * - [SwipeRefreshLayout] to allow refreshing and show refresh state. (this [LRPanelImpl] itself).
 *
 * Child view of [LRPanelImpl] added as child in XML layout is added as 3rd child (model layer) to [LR_ViewAnimator].
 * (see [LRPanelImpl.onFinishInflate]).
 */
class LRPanelImpl @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SwipeRefreshLayout(context, attrs), LoadRefreshPanel {

    companion object {
        private const val LAYER_LOADING = 0
        private const val LAYER_ERROR = 1
        private const val LAYER_MODEL = 2
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_load_refresh, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // first 2 are CircleImageView(added by SwipeRefreshLayout) && LR_ViewAnimator.
        if (childCount == 3) {
            val child = getChildAt(2)
            removeView(child)
            LR_ViewAnimator.addView(child)
        }
        if (LR_ViewAnimator.childCount < 3) {
            // if no model view was added, add empty FrameLayout.
            // so we have 3 layers.
            LR_ViewAnimator.addView(FrameLayout(context))
        }
    }

    override fun retryClicks(): Observable<Any> = RxView.clicks(LR_Retry_Button)

    override fun refreshes(): Observable<Any> = RxSwipeRefreshLayout.refreshes(this)

    override fun render(vs: LRViewState<*>) {
        if (vs.loading) {
            LR_ViewAnimator.showIfNotYet(LAYER_LOADING)
        } else if (vs.loadingError != null) {
            LR_ViewAnimator.showIfNotYet(LAYER_ERROR)
        } else {
            LR_ViewAnimator.showIfNotYet(LAYER_MODEL)
        }

        this.isRefreshing = vs.refreshing
        this.isEnabled = vs.canRefresh
    }
}