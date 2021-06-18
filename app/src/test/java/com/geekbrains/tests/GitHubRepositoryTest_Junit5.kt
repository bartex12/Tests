package com.geekbrains.tests

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.repository.RepositoryCallback
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call

//чтобы setUp() не нужно было делать статическим - такая аннотация
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GitHubRepositoryTest_Junit5 {

    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var gitHubApi: GitHubApi

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = GitHubRepository(gitHubApi)
    }

    //проверяем что 1 раз вызывается метод searchGithub интерфейса gitHubApi
    @ParameterizedTest
    @ValueSource(strings = arrayOf("a","b","c","some query","qq11234   9b 7 6767tdvb"))
    fun searchGithub_Test(searchQuery:String) {
        //val searchQuery = "some query"
        val call = Mockito.mock(Call::class.java) as Call<SearchResponse?>

        Mockito.`when`(gitHubApi.searchGithub(searchQuery)).thenReturn(call)
        repository.searchGithub(searchQuery, Mockito.mock(RepositoryCallback::class.java))
        Mockito.verify(gitHubApi, Mockito.times(1)).searchGithub(searchQuery)
    }
}