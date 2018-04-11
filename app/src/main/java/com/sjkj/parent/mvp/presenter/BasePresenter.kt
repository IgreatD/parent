package com.sjkj.parent.mvp.presenter

import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * @author by dingl on 2017/9/11.
 * @desc BasePresenter
 */
open class BasePresenter {

    private val compositeSubscription = CompositeSubscription()

    protected fun addSubscription(subscription: Subscription) {
        compositeSubscription.add(subscription)
    }

    fun unSubscription() {
        if (compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe()
    }


}
