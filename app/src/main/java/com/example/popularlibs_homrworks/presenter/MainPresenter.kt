package com.example.popularlibs_homrworks.presenter

import com.example.popularlibs_homrworks.view.main.MainView
import com.example.popularlibs_homrworks.Screens
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router

class MainPresenter(val router: Router): MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(Screens.UsersScreen())
    }

    fun backClicked() {
        router.exit()
    }
}