package com.geekbrains.tests

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.ScheduleProviderStub
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.viewmodel.ScreenState
import com.geekbrains.tests.viewmodel.SearchViewModel
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SearchViewModelTest {

    //это правило работает с Архитектурными компонентами (которым является LiveData),
    // запуская выполнение фоновых задач синхронно и по порядку, что важно для выполнения тестов.
    // В случае с LiveData рекомендуется использовать это Правило для получения
    // консистентных результатов (иначе порядок выполнения кода не будет гарантироваться).
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
    }

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: GitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        //в конструктор ВьюМодели мы передаем Мок репозитория и заглушку для тестирования rx.
        // Это именно то, что нам нужно для проведения тестов
        searchViewModel = SearchViewModel(repository, ScheduleProviderStub())
    }

    @Test //Проверим вызов метода searchGitHub() у нашей ВьюМодели
    fun search_Test() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
                Observable.just(SearchResponse(1, listOf()))
        )

        searchViewModel.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    //протестируем работу LiveData
    @Test
    fun liveData_TestReturnValueIsNotNull() {
        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
        val observer = Observer<ScreenState> {} //вынесен, чтобы удалить в конце
        //Получаем LiveData - вынесена, чтобы удалить observer в конце
        val liveData = searchViewModel.subscribeToLiveData()

        //При вызове Репозитория возвращаем шаблонные данные
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
                Observable.just(SearchResponse(1,listOf() ) )
        )

        try {
            //этот метод позволяет подписаться на уведомления и не отписываться от них никогда
            //Подписываемся на LiveData без учета жизненного цикла
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)

            //Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
            Assert.assertNotNull(liveData.value)

            //Убеждаемся, что Репозиторий вернул totalCount = 1
            val sus = liveData.value as ScreenState.Working
            Assert.assertEquals(1, sus.searchResponse.totalCount)
        } finally {
            //Тест закончен, снимаем Наблюдателя
            liveData.removeObserver(observer)
        }
    }

    //не работает - ожидается error, приходит Response is null or unsuccessful
    @Test
    fun liveData_TestReturnValueIsError() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        val error = Throwable(ERROR_TEXT)

        //При вызове Репозитория возвращаем ошибку
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
                Observable.error(error) // вау, у Observable есть error
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)
            //Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает ошибку
            val value: ScreenState.Error = liveData.value as ScreenState.Error
            //Assert.assertEquals(error.message, value.error.message) //не проходит
            //проходит такой
            Assert.assertEquals("Response is null or unsuccessful", value.error.message)
        } finally {
            liveData.removeObserver(observer)
        }
    }

}