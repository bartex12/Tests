package com.geekbrains.tests.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel(
        private val repository: RepositoryContract = GitHubRepository(
                Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(GitHubApi::class.java)),
        private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
): ViewModel() {

    companion object {
        const val BASE_URL = "https://api.github.com"
        const val TAG = "33333"
    }

    private val liveData = MutableLiveData<ScreenState>()


    fun subscribeToLiveData(): LiveData<ScreenState>{
        return liveData
    }

    fun searchGitHub(searchQuery: String) {
        //Dispose
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
                repository.searchGithub(searchQuery)
                        .subscribeOn(appSchedulerProvider.io())
                        .observeOn(appSchedulerProvider.ui())
                        .doOnSubscribe {liveData.value =  ScreenState.Loading }
                        .subscribeWith(object : DisposableObserver<SearchResponse>() {

                            override fun onNext(searchResponse: SearchResponse) {
                                //начинаем загрузку данных
                                liveData.value = ScreenState.Loading
                                val searchResults = searchResponse.searchResults
                                val totalCount = searchResponse.totalCount
                                //полезное разделение на пустой и не пустой результат
                                if (searchResults != null && totalCount != null) {
                                    liveData.value =ScreenState.Working(searchResponse)
                                } else {
                                    liveData.value =ScreenState.Error(Throwable("Search results or total count are null"))
                                }
                            }
                            //ошибка
                            override fun onError(e: Throwable) {
                                liveData.value =ScreenState.Error(Throwable("Response is null or unsuccessful"))
                            }

                            override fun onComplete() {}
                        }
                        )
        )
    }

}