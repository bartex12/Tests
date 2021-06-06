package com.example.popularlibs_homrworks.view.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.popularlibs_homrworks.R
import com.example.popularlibs_homrworks.model.State
import com.example.popularlibs_homrworks.model.StateApi
import com.example.popularlibs_homrworks.presenter.states.PresenterView
import com.example.popularlibs_homrworks.presenter.states.StatesPresenter
import com.example.popularlibs_homrworks.repository.StateRepo
import com.example.popularlibs_homrworks.view.adapter.StatesRVAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "33333"

class MainActivity: AppCompatActivity(), MainView  {

    companion object {
        const val BASE_URL = "https://restcountries.eu/rest/v2/"
    }

    private val adapter = StatesRVAdapter()
    private val presenter: PresenterView = StatesPresenter(this, createRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        presenter.getStates()
    }

    private fun initViews() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.adapter = adapter
    }

    private fun createRepository(): StateRepo {
        return StateRepo(createRetrofit().create(StateApi::class.java))
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun displayStates(states: List<State>) {
       adapter.updateResults(states)
    }

    override fun displayError() {
        Toast.makeText(this, getString(R.string.undefined_error), Toast.LENGTH_SHORT).show()
    }

    override fun displayError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun displayLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}