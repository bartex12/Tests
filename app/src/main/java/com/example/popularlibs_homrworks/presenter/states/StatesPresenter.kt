package com.example.popularlibs_homrworks.presenter.states

import android.util.Log
import com.example.popularlibs_homrworks.Screens
import com.example.popularlibs_homrworks.view.fragments.states.StatesView
import com.example.popularlibs_homrworks.model.State
import com.example.popularlibs_homrworks.model.StateRepo
import com.example.popularlibs_homrworks.view.adapter.StatesItemView
import com.example.popularlibs_homrworks.view.main.TAG
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router


//презентер для работы с фрагментом StatesFragment,  Router для навигации
class StatesPresenter(val usersRepo: StateRepo, val router: Router):
    MvpPresenter<StatesView>() {

    val statesListPresenter =
        StatesListPresenter()

    class StatesListPresenter :
        IStateListPresenter {

        val states = mutableListOf<State>()

        override var itemClickListener: ((StatesItemView) -> Unit)? = null

        override fun getCount() = states.size

        override fun bindView(view: StatesItemView) {
            val state = states[view.pos]
            view.setLogin(state.name)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadData()

        statesListPresenter.itemClickListener = { itemView ->
            //переход на экран пользователя
           val state =  statesListPresenter.states[itemView.pos].name
            Log.d(TAG, "StatesPresenter itemClickListener state =$state")
            router.replaceScreen(Screens.DetailsScreen(state))
        }
    }

    private fun loadData() {
        val users =  usersRepo.getUsers()
        statesListPresenter.states.addAll(users)
        viewState.updateList()
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}