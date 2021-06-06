package com.example.popularlibs_homrworks

import com.example.popularlibs_homrworks.model.State
import com.example.popularlibs_homrworks.repository.StateRepo
import com.example.popularlibs_homrworks.presenter.states.StatesPresenter
import com.example.popularlibs_homrworks.view.main.MainView
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import ru.terrakok.cicerone.Router

class StatesPresenterTest {
    //	Мы объявили три  переменных: Презентер и два   мока.
    //	 мы тестируем именно Презентер и ничего кроме заглушек для других классов
    //	нам не понадобится. Таким образом мы избавимся от зависимостей и лишней работы
    //	по инициализации, а также сможем протестировать Презентер в изоляции, не задействуя
    //	инстансы других классов.

    private lateinit var presenter: StatesPresenter
    @Mock
    private lateinit var mainView: MainView
    @Mock
    private lateinit var usersRepo: StateRepo

    @Before
    fun setUp(){
        //Обязательно для аннотаций "@Mock"
        MockitoAnnotations.openMocks(this)
        presenter = StatesPresenter(mainView, usersRepo)
    }

    @Test //Проверим вызов метода getStates() у  репозитория
    fun getStates_Test() {
        //Запускаем код, функционал которого хотим протестировать
        presenter.getStates()
        //Убеждаемся, что все работает как надо
       verify(usersRepo, times(1)).getStates(presenter)
    }

    @Test //Проверим вызов метода displayLoading()
    fun displayLoading_Test() {
        //Запускаем код, функционал которого хотим протестировать
        presenter.getStates()
        //Убеждаемся, что все работает как надо
        verify(mainView, times(1)).displayLoading(true)
    }

    @Test //Проверим вызов метода handleStateResponse() = Error
    fun handleStateError_TestError() {
        //Запускаем код, функционал которого хотим протестировать
        presenter.handleStateError()
        //Убеждаемся, что все работает как надо
        verify(mainView, times(1)).displayError()
    }

    @Test //Проверим вызов метода handleStateError() = Loading
    fun handleStateError_TestLoading() {
        //Запускаем код, функционал которого хотим протестировать
        presenter.handleStateError()
        //Убеждаемся, что все работает как надо
        verify(mainView, times(1)).displayLoading(false)
    }

    @Test //Проверим вызов метода handleStateResponse()
    fun handleStateResponse_TestIsSuccessful() {
        //Создаем мок ответа сервера с типом Response<List<State>>
        val response = mock(Response::class.java) as Response<List<State>?>
        //Описываем правило, что при вызове метода isSuccessful должен возвращаться false
        `when`(response.isSuccessful).thenReturn(false)
        assertFalse(response.isSuccessful)
    }

    @Test //проверим, как  обрабатываются ошибки
    fun handleStateResponse_Failure() {
        //Создаем мок ответа сервера с типом Response<List<State>?>
        val response = mock(Response::class.java) as Response<List<State>?>
        //Описываем правило, что при вызове метода isSuccessful должен возвращаться false
        //В таком случае должен вызываться метод viewContract.displayError(...)
        `when`(response.isSuccessful).thenReturn(false)

        //Вызывваем у Презентера метод handleStateResponse()
        presenter.handleStateResponse(response)

        //Убеждаемся, что вызывается верный метод: viewContract.displayError("Response is null or unsuccessful"), и что он вызывается единожды
        verify(mainView, times(1))
            .displayError("Response is null or unsuccessful")
    }

    @Test //Проверим порядок вызова методов mainView
    fun handleStateResponse_ResponseFailure_MainViewMethodOrder() {
        val response = mock(Response::class.java) as Response<List<State>?>
        `when`(response.isSuccessful).thenReturn(false)
        presenter.handleStateResponse(response)

        //Определяем порядок вызова методов какого класса мы хотим проверить
        val inOrder = Mockito.inOrder(mainView)
        //Прописываем порядок вызова методов
        inOrder.verify(mainView).displayLoading(false)
        inOrder.verify(mainView).displayError("Response is null or unsuccessful")
    }

    @Test //Проверим пустой ответ сервера
    fun handleStateResponse_ResponseIsEmpty() {
        val response = mock(Response::class.java) as Response<List<State>?>
        //когда вызван response.body(), возвращаем null
        `when`(response.body()).thenReturn(null)
        //Вызываем handleStateResponse()
        presenter.handleStateResponse(response)
        //Убеждаемся, что body == null
        assertNull(response.body())
    }

    @Test // проверим непустой ответ сервера
    fun handleStateResponse_ResponseIsNotEmpty() {
        val response = mock(Response::class.java) as Response<List<State>?>
        //мокаем непустой ответ
        `when`(response.body()).thenReturn(listOf(mock(State::class.java)))
        //или так
        //`when`(response.body()).thenReturn(listOf(State("one"),State("two")))
        //Вызываем handleStateResponse()
        presenter.handleStateResponse(response)
        //Убеждаемся, что body действительно не null
        assertNotNull(response.body())
    }

    @Test //Проверим как обрабатывается случай, если ответ от сервера пришел, но он  пустой
    fun handleStateResponse_EmptyResponse() {
        val response = mock(Response::class.java) as Response<List<State>?>
        //Устанавливаем правило, что ответ успешный
        `when`(response.isSuccessful).thenReturn(true)
        //При этом body ответа == null. В таком случае должен вызываться метод mainView.displayError(...)
        `when`(response.body()).thenReturn(null)

        //Вызываем handleGitHubResponse()
        presenter.handleStateResponse(response)

        //Убеждаемся, что вызывается верный метод: mainView.displayError("Search results or total count are null"), и что он вызывается единожды
        verify(mainView, times(1))
            .displayError("Search results or total count are null")
    }

    @Test //проверим успешный ответ
    fun handleStateResponse_Success() {
        //Мокаем ответ
        val response = mock(Response::class.java) as Response<List<State>?>
        //Мокаем тело ответа
        val searchResponse = mock(State::class.java)
        //Мокаем список
        val searchResults = listOf(mock(State::class.java))

        //Прописываем правила
        //Когда ответ приходит, возвращаем response.isSuccessful == true и
        // если response.body(), то отдаём список searchResults
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(searchResults)

        //Запускаем выполнение метода
        presenter.handleStateResponse(response)
        //Убеждаемся, что ответ от сервера обрабатывается корректно
       verify(mainView, times(1)).displayStates(searchResults)
    }

}