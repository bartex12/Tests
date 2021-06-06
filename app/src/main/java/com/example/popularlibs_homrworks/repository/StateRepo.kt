package com.example.popularlibs_homrworks.repository

import com.example.popularlibs_homrworks.model.State
import com.example.popularlibs_homrworks.model.StateApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StateRepo(private val stateApi: StateApi) {

    fun getStates(callback: StateCallback){
       val call = stateApi.getStates()
        call?.enqueue(object :Callback<List<State>?>{

            override fun onResponse(call: Call<List<State>?>, response: Response<List<State>?>) {
                callback.handleStateResponse(response)
            }

            override fun onFailure(call: Call<List<State>?>, t: Throwable) {
                callback.handleStateError()
            }
        })
    }

    interface StateCallback {
        fun handleStateResponse(response: Response< List<State>?>?)
        fun handleStateError()
    }
}