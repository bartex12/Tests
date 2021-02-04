package com.example.popularlibs_homrworks

import com.example.popularlibs_homrworks.view.fragments.details.DetailsFragment
import com.example.popularlibs_homrworks.view.fragments.states.StatesFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class StatesScreen() : SupportAppScreen() {
        override fun getFragment() = StatesFragment.newInstance()
    }
    class DetailsScreen(val state: String) : SupportAppScreen() {
        override fun getFragment() = DetailsFragment.newInstance(state)
    }
}