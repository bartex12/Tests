package com.example.popularlibs_homrworks.view.main

import android.os.Bundle
import android.util.Log
import com.example.popularlibs_homrworks.App
import com.example.popularlibs_homrworks.R
import com.example.popularlibs_homrworks.presenter.MainPresenter
import com.example.popularlibs_homrworks.view.fragments.BackButtonListener
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.android.support.SupportAppNavigator

const val TAG = "33333"

class MainActivity: MvpAppCompatActivity(), MainView  {
    val navigatorHolder = App.instance.navigatorHolder
    val navigator = SupportAppNavigator(this, supportFragmentManager,
        R.id.container
    )

    val presenter: MainPresenter by moxyPresenter { MainPresenter(App.instance.router) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    //при нажатии на кнопку Назад если фрагмент реализует BackButtonListener, вызываем метод backPressed
    //при этом если мы в списке- выходим из приложения, а если в пользователе - возвращаемся в список
    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if(it is BackButtonListener && it.backPressed()){
                return@onBackPressed
            }
        }
        Log.d(TAG, "MainActivity onBackPressed setLogin after if ")
        presenter.backClicked()
    }
}