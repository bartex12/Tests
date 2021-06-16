package com.geekbrains.tests.presenter

import com.geekbrains.tests.repository.RepositoryCallback


interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )

}