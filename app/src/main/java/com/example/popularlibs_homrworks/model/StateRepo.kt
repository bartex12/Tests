package com.example.popularlibs_homrworks.model

class StateRepo {
    private val repositories = listOf(
        State("Afghanistan"),
        State("Albania"),
        State("Algeria"),
        State("Andorra"),
        State("Angola")
    )

    fun getUsers(): List<State>{
        return repositories
    }
}