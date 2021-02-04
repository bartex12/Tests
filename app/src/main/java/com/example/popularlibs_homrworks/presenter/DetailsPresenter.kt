package com.example.popularlibs_homrworks.presenter

import android.util.Log
import com.example.popularlibs_homrworks.Screens
import com.example.popularlibs_homrworks.view.fragments.details.DetailsView
import com.example.popularlibs_homrworks.view.main.TAG
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router


class DetailsPresenter(val router: Router):
    MvpPresenter<DetailsView>()  {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun backPressed():Boolean {
        Log.d(TAG, "DetailsPresenter backPressed ")
       router.replaceScreen(Screens.StatesScreen())
        return true
    }
}
