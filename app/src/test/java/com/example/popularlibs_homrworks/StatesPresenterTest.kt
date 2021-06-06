package com.example.popularlibs_homrworks

import com.example.popularlibs_homrworks.model.StateRepo
import com.example.popularlibs_homrworks.presenter.states.StatesPresenter
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.terrakok.cicerone.Router

class StatesPresenterTest {
    //	Мы объявили четыре  переменных: Презентер и три мока.
    //	 мы тестируем именно Презентер и ничего кроме заглушек для других классов
    //	нам не понадобится. Таким образом мы избавимся от зависимостей и лишней работы
    //	по инициализации, а также сможем протестировать Презентер в изоляции, не задействуя
    //	инстансы других классов.

    private lateinit var presenter: StatesPresenter

    @Mock
    private lateinit var usersRepo: StateRepo
    @Mock
    private lateinit var router: Router


    @Before
    fun setUp(){
        //Обязательно для аннотаций "@Mock"
        MockitoAnnotations.openMocks(this)
        presenter = StatesPresenter( usersRepo, router)
    }

    @Test //Проверим вызов метода getStates() у  репозитория
    fun getStates_Test() {
        //Запускаем код, функционал которого хотим протестировать
        presenter.usersRepo.getStates()
        //Убеждаемся, что все работает как надо
       verify(usersRepo, times(1)).getStates()
    }
//
//    @Test
//    fun getStates_Success() {
//        val states = Single.just(listOf<State>())
//        `when`(presenter.statesRepo.getStates()).thenReturn(states)
//        assertEquals(presenter.statesRepo.getStates(), states)
//    }
//
//    @Test
//    fun getStates_NotNull() {
//        val states = Single.just(listOf<State>())
//        `when`(presenter.statesRepo.getStates()).thenReturn(states)
//        assertNotNull(presenter.statesRepo.getStates())
//    }
//
//    @Test
//    fun getStates_Null() {
//        val states:Single<List<State>>? = null
//        `when`(presenter.statesRepo.getStates()).thenReturn(states)
//        assertNull(presenter.statesRepo.getStates())
//    }

}