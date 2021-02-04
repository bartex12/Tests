package com.example.popularlibs_homrworks.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.popularlibs_homrworks.R
import com.example.popularlibs_homrworks.presenter.states.IStateListPresenter
import com.example.popularlibs_homrworks.view.main.TAG
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_state.view.*


class StatesRVAdapter(val presenter: IStateListPresenter)
    : RecyclerView.Adapter<StatesRVAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_state, parent, false )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = presenter.getCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.pos =position
        presenter.bindView(holder)

        holder.containerView.setOnClickListener{
            //нельзя иначе осуществить вызов nullable-значения функционального типа.
            //вызовет itemClickListener, если он не равен null
            presenter.itemClickListener?.invoke(holder)
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer,
        StatesItemView {

        override var pos = -1

        override fun setLogin(text: String) {
            containerView.tv_login.text = text
            Log.d(TAG, "StatesRVAdapter ViewHolder setLogin text =$text")
        }
    }
}