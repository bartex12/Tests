package com.example.popularlibs_homrworks.view.fragments.states

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.popularlibs_homrworks.App
import com.example.popularlibs_homrworks.R
import com.example.popularlibs_homrworks.model.StateRepo
import com.example.popularlibs_homrworks.presenter.states.StatesPresenter
import com.example.popularlibs_homrworks.view.adapter.StatesRVAdapter
import com.example.popularlibs_homrworks.view.fragments.BackButtonListener
import kotlinx.android.synthetic.main.fragment_states.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter


class StatesFragment : MvpAppCompatFragment(),
    StatesView,
    BackButtonListener {

    companion object { fun newInstance() =
        StatesFragment()
    }

    val presenter: StatesPresenter by moxyPresenter {
        StatesPresenter(
            StateRepo(),
            App.instance.router
        )
    }

    var adapter: StatesRVAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        View.inflate(context, R.layout.fragment_states, null)

    override fun init() {
        rv_states.layoutManager = LinearLayoutManager(context)
        adapter = StatesRVAdapter( presenter.statesListPresenter )
        rv_states.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun backPressed() = presenter.backPressed()

}