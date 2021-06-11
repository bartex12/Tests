package com.geekbrains.tests

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.search.MainActivity
import junit.framework.TestCase
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
    //1
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

    @Test
    //2 MainActivity NotNull
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test
    //3 MainActivity в RESUMED
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    //4 searchEditText  NotNull
    fun activityEditText_NotNull() {
        scenario.onActivity {
         val searchEditText =   it.findViewById<EditText>(R.id.searchEditText)
            TestCase.assertNotNull(searchEditText)
        }
    }

    @Test
    //5 - тест с использованием espresso
    //в searchEditText вводится то, что вводим
    fun activityEditText_TextNotNull() {
        //Выделяем по тапу наш EditText
        onView(withId(R.id.searchEditText)).perform(click())
        //Вставляем в него текст запроса
        onView(withId(R.id.searchEditText)).perform(replaceText("one"), closeSoftKeyboard())
        //на вью с id searchEditText проконтролировать соответствие тексту one
        onView(withId(R.id.searchEditText)).check(matches(withText("one")))
    }

    @Test
    //6 -убедимся, что текст отображается на экране.
    fun activityEditText_IsDisplayed() {
        //на вью с id searchEditText проконтролировать что она  отображается на экране
        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()))
    }

    @Test
    //7 - убедимся, что кнопка отображается на экране полностью
    fun activityButtonToDetail_IsCompletelyDisplayed() {
        //на вью с id toDetailsActivityButton проконтролировать что кнопка полностью отображается на экране
        onView(withId(R.id.toDetailsActivityButton)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    //8 - убедимся, что надпись на кнопке правильная
    fun activityButtonToDetail_IsCorrect() {
        //на вью с id toDetailsActivityButton проконтролировать что текст отображается корректно
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withText("to details")))
    }

    @Test
    //9- проверяем видимость элементов на экране
    //Метод withEffectiveVisibility() возвращает VISIBLE только если все вью в иерархии видны
    fun activityButtons_AreEffectiveVisible() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.searchEditText)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @After
    fun close() {
        scenario.close()
    }
}