package com.example.popularlibs_homrworks.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.popularlibs_homrworks.R
import com.example.popularlibs_homrworks.model.State


class StatesRVAdapter()
    : RecyclerView.Adapter<StatesRVAdapter.ViewHolder> () {

    private var states: List<State> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_state, parent, false )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = states.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

     holder.bind(states[position])
    }

    fun updateResults(states: List<State>) {
        this.states = states
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        fun bind(state: State) {
            itemView.findViewById<TextView>(R.id.tv_name).text = state.name
        }
      }
}
