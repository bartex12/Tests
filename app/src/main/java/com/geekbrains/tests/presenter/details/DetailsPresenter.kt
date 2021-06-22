package com.geekbrains.tests.presenter.details

import android.util.Log
import com.geekbrains.tests.view.ViewContract
import com.geekbrains.tests.view.details.DetailsFragment
import com.geekbrains.tests.view.details.ViewDetailsContract


internal class DetailsPresenter
    : PresenterDetailsContract {

    companion object{
        const val TAG = "33333"
    }

    var count:Int = 0
     var viewContract: ViewDetailsContract? = null

    override fun onAttach(view: ViewContract?) {
        viewContract = view as ViewDetailsContract
    }

    override fun onDetach() {
        viewContract = null
    }

    override fun setCounter(count: Int) {
        Log.d(TAG, "DetailsPresenter setCounter: count = $count")
        this.count = count
    }

    override fun onIncrement() {
        Log.d(TAG, "DetailsPresenter onIncrement: count before= $count")
        count++
        viewContract?.setCount(count)
        Log.d(TAG, "DetailsPresenter onIncrement: count after= $count")
    }

    override fun onDecrement() {
        count--
        viewContract?.setCount(count)
    }
}
