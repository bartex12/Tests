package com.example.popularlibs_homrworks.view.fragments.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.popularlibs_homrworks.App
import com.example.popularlibs_homrworks.R
import com.example.popularlibs_homrworks.presenter.DetailsPresenter
import com.example.popularlibs_homrworks.view.fragments.BackButtonListener
import com.example.popularlibs_homrworks.view.main.TAG
import kotlinx.android.synthetic.main.fragment_details.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter


class DetailsFragment : MvpAppCompatFragment(),
    DetailsView,
    BackButtonListener {

    companion object {
        private const val ARG_STATE = "state"

        @JvmStatic
        fun newInstance(state: String) =
            DetailsFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_STATE, state)
                }
            }
    }

    private var state: String? = null

    val presenter: DetailsPresenter by moxyPresenter {
        DetailsPresenter(App.instance.router) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            state = it.getString(ARG_STATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    //реализация метода DetailsView
    override fun init() {
        state?. let {
            tv_state_name.text = it}?:let {tv_state_name.text = ""
        }
    }

    //реализация метода BackButtonListener
    override fun backPressed(): Boolean = presenter.backPressed()

}