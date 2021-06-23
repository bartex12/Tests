package com.geekbrains.tests.espresso

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.R
import com.geekbrains.tests.TEST_NUMBER_OF_RESULTS_PLUS_1
import com.geekbrains.tests.view.details.DetailsFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) //эта аннотация не обязательна
class DetailsFragmentEspressoTest {

    private lateinit var  scenario :FragmentScenario<DetailsFragment>

    @Before
    fun setup(){
        //Запускаем Fragment в корне Activity
        //launchFragmentInContainer() нужен для запуска Фрагмента с UI;
        //launchFragment — для Фрагментов без UI.
        scenario = launchFragmentInContainer()
    }

    @Test
    //создаем бандл с аргументами и создаем новый сценарий, который принимает аргументы.
    //сценарий по умолчанию переводит Фрагмент в State.RESUMED, тут это написано просто в качестве примера.
//Далее мы проверяем, что отображается строка в соответствии с переданными аргументами.
    fun fragment_testBundle() {
        //Можно передавать аргументы во Фрагмент, но это необязательно
        val fragmentArgs = bundleOf("TOTAL_COUNT_EXTRA" to 10)
        //Запускаем Fragment с аргументами
        val scenario = launchFragmentInContainer<DetailsFragment>(fragmentArgs)
        //Возможность менять стейт Фрагмента
        scenario.moveToState(Lifecycle.State.RESUMED)

        //на вью  с id = totalCountTextView проверить соответствие тексту "Number of results: 10"
        onView(withId(R.id.totalCountTextView))
            .check(matches(withText("Number of results: 10")))
    }

    @Test
    fun fragment_testSetCountMethod() {
        //в методичке ошибка - onFragment вместо withFragment
        scenario.withFragment {
         setCount(10)
        }
        val assertion = matches(withText("Number of results: 10"))
        onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test
    fun fragment_testIncrementButton() {
        onView(withId(R.id.incrementButton)).perform(click())
        onView(withId(R.id.totalCountTextView)).check(matches(withText(TEST_NUMBER_OF_RESULTS_PLUS_1)))
    }

    // scenario.close() не требуется для фрагментов ?

}