package com.example.popularlibs_homrworks.view.main

import com.example.popularlibs_homrworks.model.State

interface MainView{

    fun displayStates(states: List<State>)

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}