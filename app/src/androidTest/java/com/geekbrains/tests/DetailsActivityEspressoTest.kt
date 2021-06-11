package com.geekbrains.tests

import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.details.DetailsActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsActivityEspressoTest {

    lateinit var  scenario: ActivityScenario<DetailsActivity>

    @Before
    fun setUp(){
        scenario = ActivityScenario.launch(DetailsActivity::class.java)
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
           TestCase.assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            TestCase.assertNotNull(totalCountTextView)
        }

    }

    @Test
    //тест с использованием espresso
    fun activityTextView_HasText() {
        //на вью с id totalCountTextView проконтролировать соответствие тексту Number of results: 0
       onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 0")))
    }

    @Test
    //убедимся, что текст отображается на экране.
    fun activityTextView_IsDisplayed() {
        //на вью с id totalCountTextView проконтролировать что текст отображается на экране
        onView(withId(R.id.totalCountTextView)).check(matches(isDisplayed()))
    }

    @Test
    //убедимся, что текст отображается на экране полностью
    fun activityTextView_IsCompletelyDisplayed() {
        //на вью с id totalCountTextView проконтролировать что текст полностью отображается на экране
        onView(withId(R.id.totalCountTextView)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    //убедимся, что длинный текст отображается на экране полностью
    fun activityTextView_IsCompletelyDisplayedLong() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            totalCountTextView.setText("111111111111111111111111111111111111111111111111",
                TextView.BufferType.EDITABLE)
        }
        //на вью с id totalCountTextView проконтролировать что текст полностью отображается на экране
        onView(withId(R.id.totalCountTextView)).check(matches(isCompletelyDisplayed()))
    }


    //Здесь для проверки видимости мы используем подход с проверкой флага Visibility у View:
    // проверяем у кнопки именно этот параметр с помощью специального метода withEffectiveVisibility.
    //Метод withEffectiveVisibility() возвращает VISIBLE только если все вью в иерархии видны
    @Test
    fun activityButtons_AreEffectiveVisible() {
        onView(withId(R.id.decrementButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.incrementButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    //Проверим как у нас отрабатываются нажатия на кнопки
    //Тут мы выбираем нужную нам кнопку и имитируем нажатие пользователя. И убеждаемся,
    // что TextView меняется как надо.
    @Test
    fun activityButtonIncrement_IsWorking() {
        onView(withId(R.id.incrementButton)).perform(click())
        onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 1")))
    }

    @Test
    fun activityButtonDecrement_IsWorking() {
        onView(withId(R.id.decrementButton)).perform(click())
        onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: -1")))
    }

    @After
    fun close(){
        scenario.close()
    }

}