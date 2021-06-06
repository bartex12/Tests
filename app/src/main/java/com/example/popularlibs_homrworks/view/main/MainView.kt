package com.example.popularlibs_homrworks.view.main

import com.example.popularlibs_homrworks.model.State
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView{

    fun displayStates(states: List<State>)

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}