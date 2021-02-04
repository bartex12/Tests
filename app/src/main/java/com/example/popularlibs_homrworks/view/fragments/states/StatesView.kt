package com.example.popularlibs_homrworks.view.fragments.states

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface StatesView: MvpView {
    fun init()
    fun updateList()
}