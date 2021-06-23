package com.geekbrains.tests.presenter.search

import android.util.Log
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.RepositoryCallback
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.view.ViewContract
import com.geekbrains.tests.view.search.SchedulerProvider
import com.geekbrains.tests.view.search.SearchSchedulerProvider
import com.geekbrains.tests.view.search.ViewSearchContract
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.Response

/**
 * В архитектуре MVP все запросы на получение данных адресуются в Репозиторий.
 * Запросы могут проходить через Interactor или UseCase, использовать источники
 * данных (DataSource), но суть от этого не меняется.
 * Непосредственно Презентер отвечает за управление потоками запросов и ответов,
 * выступая в роли регулировщика движения на перекрестке.
 */

internal class SearchPresenter internal constructor(
    private val repository: RepositoryContract,
    private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
) : PresenterSearchContract, RepositoryCallback {

    companion object{
        const val TAG = "33333"
    }

     var  viewContract: ViewSearchContract? = null
     private var compositeDisposable:CompositeDisposable = CompositeDisposable()

    //*** метод интерфейса PresenterSearchContract -> PresenterContract
    override fun onAttach(view: ViewContract?) {
        viewContract = view as ViewSearchContract
    }

    override fun searchGitHub(searchQuery: String) {
        //Dispose
        compositeDisposable.add(
            repository.searchGithub(searchQuery)
                .subscribeOn(appSchedulerProvider.io())
                .observeOn(appSchedulerProvider.ui())
                .doOnSubscribe { viewContract?.displayLoading(true) }
                .doOnTerminate { viewContract?.displayLoading(false) }
                .subscribeWith(object : DisposableObserver<SearchResponse>() {

                    override fun onNext(searchResponse: SearchResponse) {
                        val searchResults = searchResponse.searchResults
                        val totalCount = searchResponse.totalCount
                        if (searchResults != null && totalCount != null) {
                            viewContract?.displaySearchResults(
                                searchResults,
                                totalCount
                            )
                        } else {
                            viewContract?.displayError("Search results or total count are null")
                        }
                    }

                    override fun onError(e: Throwable) {
                        viewContract?.displayError(e.message ?: "Response is null or unsuccessful")
                    }

                    override fun onComplete() {}
                }
                )
        )
    }

    //*** метод интерфейса PresenterSearchContract -> PresenterContract
    override fun onDetach() {
        viewContract = null
        compositeDisposable.clear()
    }

    //### метод интерфейса RepositoryCallback - в Rx код не работает, но удалять нельзя
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

    //### метод интерфейса RepositoryCallback - в Rx код не работает, но удалять нельзя
    override fun handleGitHubError() {
        viewContract?.displayLoading(false)
        viewContract?.displayError()
    }
}
