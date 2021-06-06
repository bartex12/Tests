package com.example.popularlibs_homrworks.model

import retrofit2.Call
import retrofit2.http.GET

interface StateApi {

    @GET("all")
    fun getStates(): Call< List<State>?>?
}