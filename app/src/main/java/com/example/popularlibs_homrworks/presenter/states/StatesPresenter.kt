package com.example.popularlibs_homrworks.presenter.states

import com.example.popularlibs_homrworks.model.State
import com.example.popularlibs_homrworks.repository.StateRepo
import com.example.popularlibs_homrworks.view.main.MainView
import retrofit2.Response


//презентер для работы с фрагментом StatesFragment,  Router для навигации
class StatesPresenter(private val mainView: MainView, val stateRepo: StateRepo):
    PresenterView, StateRepo.StateCallback {

    override fun getStates() {
        mainView.displayLoading(true)
        stateRepo.getStates(this)
    }

    override fun handleStateResponse(response: Response<List<State>?>?) {
        mainView.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val states = response.body()
            if (states != null) {
                mainView.displayStates(states)
            } else {
                mainView.displayError("Search results or total count are null")
            }
        } else {
            mainView.displayError("Response is null or unsuccessful")
        }
    }

    override fun handleStateError() {
        mainView.displayLoading(false)
        mainView.displayError()
    }
}