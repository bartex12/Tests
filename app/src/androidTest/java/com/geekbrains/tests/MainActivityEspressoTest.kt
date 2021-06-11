package com.geekbrains.tests

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.search.MainActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun  activitySearch_IsWorking(){
        //Выделяем по тапу наш EditText
        onView(withId(R.id.searchEditText)).perform(click())
        //Вставляем в него текст запроса
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        //Нажимаем на кнопку поиска на виртуальной клавиатуре
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        if (BuildConfig.TYPE == MainActivity.FAKE) {
            onView(withId(R.id.totalCountTextViewMain)).check(matches(withText("Number of results: 42")))
        }else{
            //“замораживаем” UI на 2 секунды.
            onView(isRoot()).perform(delay())
            //Сравниваем отображаемый текст с ожидаемым результатом.
            onView(withId(R.id.totalCountTextViewMain)).check(matches(withText("Number of results: 2415")))
        }
    }

    //Будем возвращать свой собственный ViewAction, переопределив методы интерфейса,
    // который используется в методе perform():
    private fun delay(): ViewAction? {
        return object : ViewAction {
            //getConstraints() будет возвращать root-view нашей кнопки;
            override fun getConstraints(): Matcher<View> = isRoot()
            //getDescription() возвращает описание нашего Action
            override fun getDescription(): String = "wait for $2 seconds"
            //perform() занимается непосредственно Action’ом: нас интересует UiController, у
            // которого есть нужный нам метод. Мы “замораживаем” UI на 2 секунды
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }


}