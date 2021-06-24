package com.geekbrains.tests

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.search.SearchPresenter
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.presenter.ScheduleProviderStub
import com.geekbrains.tests.view.search.ViewSearchContract
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class SearchPresenterTestRx {

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
    }

    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        //Создаем Презентер, используя моки Репозитория и Вью, проинициализированные строкой выше
        //ScheduleProviderStub- это специальный класс для тестирования. Обычные scheduler’ы для
        // Unit-тестов не подходят — при попытке запустить тест он будет падать с NPE.
        // Специально для тестирования в rx есть метод Schedulers.trampoline(),
        // который позволяет запускать код без крашей
        presenter = SearchPresenter(repository, ScheduleProviderStub())

        presenter.viewContract = mock(ViewSearchContract::class.java)
        viewContract =  presenter.viewContract as ViewSearchContract
    }

    @Test //Проверим вызов метода searchGitHub() у нашего Репозитория
    fun searchGitHub_Test() {
        //правило такое
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse( 1, listOf() ))
        )

        presenter.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test //Проверяем как обрабатывается ошибка запроса
    fun handleRequestError_Test() {
        //правило такое
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.error(Throwable(ERROR_TEXT))
        )

        presenter.searchGitHub(SEARCH_QUERY)
        verify(viewContract, times(1)).displayError("error")
    }

    @Test //Проверяем как обрабатываются неполные данные
    fun handleResponseError_TotalCountIsNull() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    listOf()
                )
            )
        )

        presenter.searchGitHub(SEARCH_QUERY)
        verify(viewContract, times(1)).displayError("Search results or total count are null")
    }

    @Test //Проверим порядок вызова методов viewContract при ошибке
    fun handleResponseError_TotalCountIsNull_ViewContractMethodOrder() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    listOf()
                )
            )
        )

        presenter.searchGitHub(SEARCH_QUERY)

        val inOrder = inOrder(viewContract)
        inOrder.verify(viewContract).displayLoading(true)
        inOrder.verify(viewContract).displayError("Search results or total count are null")
        inOrder.verify(viewContract).displayLoading(false)
    }

    @Test //Теперь проверим успешный ответ сервера
    fun handleResponseSuccess() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    42,
                    listOf()
                )
            )
        )

        presenter.searchGitHub(SEARCH_QUERY)
        verify(viewContract, times(1)).displaySearchResults(listOf(), 42)
    }


}