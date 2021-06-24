package com.geekbrains.tests.viewmodel

import com.geekbrains.tests.model.SearchResponse

sealed class ScreenState {
    object Loading : ScreenState() // чтобы без конструктора можно было
    data class Working(val searchResponse: SearchResponse): ScreenState()
    data class Error(val error: Throwable): ScreenState()
}