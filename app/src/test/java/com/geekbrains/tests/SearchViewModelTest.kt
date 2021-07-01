package com.geekbrains.tests

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.viewmodel.ScreenState
import com.geekbrains.tests.viewmodel.SearchViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
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

    //правило для тестирования корутин
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "Search results or total count are null"
        private const val EXCEPTION_TEXT = "Response is null or unsuccessful"
    }

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: GitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        //в конструктор ВьюМодели мы передаем Мок репозитория и заглушку для тестирования rx.
        // Это именно то, что нам нужно для проведения тестов
        searchViewModel = SearchViewModel(repository)
    }

    //********** Проверим вызов метода searchGithubAsync() у нашего Репозитория ***********
    @Test
    fun coroutines_Test_HW() {
        testCoroutineRule.runBlockingTest {
            //При вызове Репозитория возвращаем шаблонные данные
            `when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                    SearchResponse(1, listOf())
            )

            searchViewModel.searchGitHub(SEARCH_QUERY)
            verify(repository, times(1)).searchGithubAsync(SEARCH_QUERY)
        }
    }

    @Test
    fun coroutines_TestReturnValueIsNotNull() {
        testCoroutineRule.runBlockingTest {
            //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
             //так как мы проверяем работу LiveData и не собираемся ничего делать с данными,
             //которые она возвращает
            val observer = Observer<ScreenState> {}
            //Получаем LiveData - вынесена, чтобы удалить observer в конце
            val liveData = searchViewModel.subscribeToLiveData()

            //При вызове Репозитория возвращаем шаблонные данные
            `when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(1, listOf())
            )

            try {
                //этот метод позволяет подписаться на уведомления и не отписываться от них никогда
                //Подписываемся на LiveData без учета жизненного цикла
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                //Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
                Assert.assertNotNull(liveData.value)

            } finally {
                //Тест закончен, снимаем Наблюдателя
                liveData.removeObserver(observer)
            }
        }
    }

    //********** Проверим что вернулось правильное значение totalCount *************
    @Test
    fun coroutines_TestReturnRightValue_HW() {
        testCoroutineRule.runBlockingTest {
            //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
            //так как мы проверяем работу LiveData и не собираемся ничего делать с данными,
            //которые она возвращает
            val observer = Observer<ScreenState> {}
            //Получаем LiveData - вынесена, чтобы удалить observer в конце
            val liveData = searchViewModel.subscribeToLiveData()

            //При вызове Репозитория возвращаем шаблонные данные
            `when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                    SearchResponse(1, listOf())
            )

            try {
                //этот метод позволяет подписаться на уведомления и не отписываться от них никогда
                //Подписываемся на LiveData без учета жизненного цикла
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val result = liveData.value as ScreenState.Working
                //Убеждаемся, что Репозиторий вернул totalCount = 1
                Assert.assertEquals(1, result.searchResponse.totalCount)
            } finally {
                //Тест закончен, снимаем Наблюдателя
                liveData.removeObserver(observer)
            }
        }
    }

    //********** Проверим что вернулось правильное значение размера списка *************
    @Test
    fun coroutines_TestReturnRightListSize_HW() {
        testCoroutineRule.runBlockingTest {
            //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
            //так как мы проверяем работу LiveData и не собираемся ничего делать с данными,
            //которые она возвращает
            val observer = Observer<ScreenState> {}
            //Получаем LiveData - вынесена, чтобы удалить observer в конце
            val liveData = searchViewModel.subscribeToLiveData()

            //При вызове Репозитория возвращаем шаблонные данные
            `when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                    SearchResponse(1, listOf())
            )

            try {
                //этот метод позволяет подписаться на уведомления и не отписываться от них никогда
                //Подписываемся на LiveData без учета жизненного цикла
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val result = liveData.value as ScreenState.Working
                //Убеждаемся, что Репозиторий вернул totalCount = 1
                Assert.assertEquals(0, result.searchResponse.searchResults?.size)
            } finally {
                //Тест закончен, снимаем Наблюдателя
                liveData.removeObserver(observer)
            }
        }
    }

    //проверяем обработку ошибки если totalCount = null
    @Test
    fun coroutines_TestReturnValueIsError() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            `when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(null, listOf())
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, ERROR_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    //************* проверяем обработку ошибки если список = null **************
    @Test
    fun coroutines_TestReturnListIsError_HW() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            `when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                    SearchResponse(1, null)
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, ERROR_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    //
    //Для того чтобы проверить, как у нас обрабатываются выбрасываемые исключения,
    // выполним вышеописанный код, но без вызова методов Репозитория.
    // То есть метод SearchViewModel.searchGitHub(query) вызовется,
    // а метод repository.searchGithubAsync(query) — нет. Это приведет
    // к некорректной работе Корутин и наш CoroutineExceptionHandler выбросит исключение,
    // вызвав метод handleError(throwable) и, соответственно,
    // liveData.value = ScreenState.Error(...):

    @Test
    fun coroutines_TestException() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, EXCEPTION_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

}