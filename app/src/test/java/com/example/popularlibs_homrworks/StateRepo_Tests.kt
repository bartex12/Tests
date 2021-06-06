package com.example.popularlibs_homrworks

import com.example.popularlibs_homrworks.model.State
import com.example.popularlibs_homrworks.model.StateApi
import com.example.popularlibs_homrworks.repository.StateRepo
import okhttp3.Request
import okio.Timeout
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StateRepo_Tests {

    lateinit var repo: StateRepo

    @Mock
    private lateinit var stateApi: StateApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repo = StateRepo(stateApi)
    }

    @Test
    fun getStates_Test() {
        //заменяем моком
        val call = mock(Call::class.java) as Call<List<State>?>
        //формируем условие
        `when`(stateApi.getStates()).thenReturn(call)
        //вызываем getStates, заменяя StateCallback моком
        repo.getStates(mock(StateRepo.StateCallback::class.java))
        //проверяем, что stateApi вызывает getStates 1 раз
        verify(stateApi, times(1)).getStates()

    }

    @Test
    fun getStates_TestCallback() {
        val response = mock(Response::class.java) as Response<List<State>?>
        val stateCallBack = mock(StateRepo.StateCallback::class.java)

        val call = object : Call<List<State>?> {
            override fun enqueue(callback: Callback<List<State>?>) {
                callback.onResponse(this, response)
                callback.onFailure(this, Throwable())
            }

            override fun clone(): Call<List<State>?> {
                TODO("Not yet implemented")
            }

            override fun execute(): Response<List<State>?> {
                TODO("Not yet implemented")
            }

            override fun isExecuted(): Boolean {
                TODO("Not yet implemented")
            }

            override fun cancel() {
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun request(): Request {
                TODO("Not yet implemented")
            }

            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }

        `when`(stateApi.getStates()).thenReturn(call)
        repo.getStates(stateCallBack)

        verify(stateCallBack, times(1)).handleStateResponse(response)
        verify(stateCallBack, times(1)).handleStateError()
    }
}