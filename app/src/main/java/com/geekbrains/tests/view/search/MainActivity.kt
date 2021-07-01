package com.geekbrains.tests.view.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.tests.R
import com.geekbrains.tests.view.details.DetailsActivity
import com.geekbrains.tests.viewmodel.ScreenState
import com.geekbrains.tests.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*


class   MainActivity : AppCompatActivity() {

    private val adapter = SearchResultAdapter()
    private var totalCount: Int = 0
    lateinit var viewModel:SearchViewModel

    companion object {
        const val TAG = "33333"
        const val FAKE = "FAKE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        setUI()
        viewModel.subscribeToLiveData() //метод SearchViewModel
                .observe(this, Observer<ScreenState> {
                    onStateChange(it)
                })
    }

    private fun onStateChange(screenState: ScreenState){

        when(screenState){
            is ScreenState.Working -> {
                progressBar.visibility = View.GONE

                val searchResponse = screenState.searchResponse
                val totalCount = searchResponse.totalCount
                Log.d(TAG, "*** MainActivity onStateChange totalCount = $totalCount")

                with(totalCountTextViewMain){
                    visibility = View.VISIBLE
                    //хитрая запись Number of results: %d
                    text = String.format(getString(R.string.results_count), totalCount)
                }

                this.totalCount = totalCount!! //проверки были во ViewModel
                adapter.updateResults(searchResponse.searchResults!!)  //проверки были во ViewModel
            }
            is ScreenState.Loading -> {
                progressBar.visibility = View.VISIBLE
            }

            is ScreenState.Error -> {
                progressBar.visibility = View.GONE
                Toast.makeText(this, screenState.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUI() {
        toDetailsActivityButton.setOnClickListener {
            Log.d(TAG, "setUI setOnClickListener totalCount = $totalCount")
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
        setSearchListener()
        setQueryListener()
        setRecyclerView()
    }

    private fun setSearchListener() {
        searchMainButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotBlank()) {
                viewModel.searchGitHub(query)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.enter_search_word),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun setQueryListener() {
        searchEditText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString()
                if (query.isNotBlank()) {
                    viewModel.searchGitHub(query)
                    return@OnEditorActionListener true
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.enter_search_word),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnEditorActionListener false
                }
            }
            false
        })
    }
}
