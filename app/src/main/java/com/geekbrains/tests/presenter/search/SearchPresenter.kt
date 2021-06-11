package com.geekbrains.tests.presenter.search

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.RepositoryCallback
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.view.ViewContract
import com.geekbrains.tests.view.search.ViewSearchContract
import retrofit2.Response

/**
 * В архитектуре MVP все запросы на получение данных адресуются в Репозиторий.
 * Запросы могут проходить через Interactor или UseCase, использовать источники
 * данных (DataSource), но суть от этого не меняется.
 * Непосредственно Презентер отвечает за управление потоками запросов и ответов,
 * выступая в роли регулировщика движения на перекрестке.
 */

internal class SearchPresenter internal constructor(
    private val repository: RepositoryContract
) : PresenterSearchContract, RepositoryCallback {

    companion object{
        const val TAG = "33333"
    }

     var  viewContract: ViewSearchContract? = null

    //***метод интерфейса PresenterSearchContract
    override fun searchGitHub(searchQuery: String) {
        viewContract?.displayLoading(true)
        //$$$ метод интерфейса RepositoryContract
        repository.searchGithub(searchQuery, this)
    }

    //*** метод интерфейса PresenterSearchContract -> PresenterContract
    override fun onAttach(view: ViewContract?) {
        viewContract = view as ViewSearchContract
        //Log.d(TAG, "onAttach: viewContract = $viewContract ")
    }

    //*** метод интерфейса PresenterSearchContract -> PresenterContract
    override fun onDetach() {
        viewContract = null
        //Log.d(TAG, "onDetach: viewContract = $viewContract ")
    }

    //### метод интерфейса RepositoryCallback
    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        viewContract?.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val searchResponse = response.body()
            val searchResults = searchResponse?.searchResults
            val totalCount = searchResponse?.totalCount
            if (searchResults != null && totalCount != null) {
                viewContract?.displaySearchResults(
                        searchResults,
                        totalCount
                )
            } else {
                viewContract?.displayError("Search results or total count are null")
            }
        } else {
            viewContract?.displayError("Response is null or unsuccessful")
        }
    }

    //### метод интерфейса RepositoryCallback
    override fun handleGitHubError() {
        viewContract?.displayLoading(false)
        viewContract?.displayError()
    }
}
