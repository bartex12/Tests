package com.geekbrains.tests

import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.view.ViewContract
import com.geekbrains.tests.view.details.ViewDetailsContract
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailPresenterTest {

    private lateinit var presenter: DetailsPresenter
    @Mock
    private lateinit var viewContract: ViewDetailsContract

    @Before
    fun setUp() {
        //Обязательно для аннотаций "@Mock"
        MockitoAnnotations.initMocks(this)

        //Создаем Презентер, используя моки Репозитория и Вью, проинициализированные строкой выше
        presenter = DetailsPresenter()
        presenter.viewContract = Mockito.mock(ViewDetailsContract::class.java)
        viewContract =  presenter.viewContract as ViewDetailsContract
    }

    @Test
    fun onAttach_AssertNotNull(){
        val   view: ViewContract = Mockito.mock(ViewDetailsContract::class.java)
        presenter.onAttach(view )
        Assert.assertNotNull(presenter.viewContract)
    }

    @Test
    fun onDetach_AssertNull(){
        presenter.onDetach()
        Assert.assertNull(presenter.viewContract)
    }

    @Test
    fun setCounter_Count33() {
        presenter.setCounter(33)
        assertEquals(33, presenter.count )
    }

    @Test
    fun onIncrement_Test() {
        presenter.onIncrement()
        assertEquals(presenter.count, 1)
        Mockito.verify(presenter.viewContract, Mockito.times(1))?.setCount(1)
    }

    @Test
    fun onDecrement_Test() {
        presenter.onDecrement()
        assertEquals(presenter.count, -1)
        Mockito.verify(presenter.viewContract, Mockito.times(1))?.setCount(-1)
    }

}