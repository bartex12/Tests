package com.example.popularlibs_homrworks.presenter

import android.util.Log
import com.example.popularlibs_homrworks.Screens
import com.example.popularlibs_homrworks.view.fragments.UserView
import com.example.popularlibs_homrworks.view.main.TAG
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router


class UserPresenter(val router: Router):
    MvpPresenter<UserView>()  {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun backPressed():Boolean {
       // router.navigateTo(Screens.UsersScreen()) //циклический обход - завис
        router.replaceScreen(Screens.UsersScreen())
        Log.d(TAG, "UserPresenter backPressed ")
        return true
    }
}
