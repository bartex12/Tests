package com.geekbrains.tests.presenter

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.RepositoryCallback
import io.reactivex.Observable


interface RepositoryContract {
    //запрос с использованием колбека
    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )
     //поменяем контракт нашего репозитория — добавим в него метод для запроса через rx,
     // который будет возвращать Observable. Обратите внимание, что мы не передаем
     // в новый метод никаких колбеков, так как мы работаем с потоком
    fun searchGithub(
        query: String
    ): Observable<SearchResponse>

    //метод, работающий с Корутинами:
    suspend fun searchGithubAsync(
        query: String
    ): SearchResponse
}