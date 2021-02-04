package com.example.popularlibs_homrworks.view.fragments.capitalweather

import com.example.popularlibs_homrworks.view.fragments.BackButtonListener
import moxy.MvpAppCompatFragment

class WeatherFragment : MvpAppCompatFragment(),
    WeatherView,
    BackButtonListener {
    override fun init() {
        //TODO
    }

    override fun backPressed(): Boolean {
        //TODO
        return true
    }
}