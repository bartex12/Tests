package com.geekbrains.tests

import com.geekbrains.tests.presenter.search.SearchPresenter
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.view.search.ViewSearchContract
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


//чтобы setUp() не нужно было делать статическим - такая аннотация
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchPresenterTest_Junits5 {

    private lateinit var presenter: SearchPresenter
    @Mock
    private lateinit var repository: GitHubRepository
    @Mock
    private lateinit var viewContract: ViewSearchContract

    //так как тест повторяем, нужно инициализацию делать каждый раз
    @BeforeEach
    fun setUp() {
        //Обязательно для аннотаций "@Mock"
        MockitoAnnotations.initMocks(this)

        //Создаем Презентер, используя моки Репозитория и Вью, проинициализированные строкой выше
        presenter = SearchPresenter( repository)
        presenter.viewContract = Mockito.mock(ViewSearchContract::class.java)
        viewContract =  presenter.viewContract as ViewSearchContract
    }

    @RepeatedTest(5) //Проверим вызов метода searchGitHub() у нашего Репозитория
    fun searchGitHub_Test() {
        val searchQuery = "some query"
        //Запускаем код, функционал которого хотим протестировать
        presenter.searchGitHub("some query")
        //Убеждаемся, что все работает как надо
        Mockito.verify(repository, Mockito.times(1)).searchGithub(searchQuery, presenter)
    }
}