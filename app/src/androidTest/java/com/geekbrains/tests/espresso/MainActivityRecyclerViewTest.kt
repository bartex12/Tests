package com.geekbrains.tests.espresso

import android.view.View
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.BuildConfig
import com.geekbrains.tests.R
import com.geekbrains.tests.view.search.MainActivity
import com.geekbrains.tests.view.search.SearchResultAdapter
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityRecyclerViewTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activitySearch_ScrollTo() {
        if (BuildConfig.TYPE == MainActivity.FAKE) {
            loadList()

            //находим наш RecyclerView по id и вызываем у него Action scrollTo, который стал
            // нам доступен благодаря новой зависимости RecyclerViewActions.
            // Метод hasDescendant ищет вью с надписью “FullName: 42”, то есть 42 элемент списка.
            // И если находит, то проматывает список до этого элемента.
            // То же самое можно сделать с помощью метода scrollToPosition,
            // передавая индекс нужного элемента в качестве аргумента.
            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                        hasDescendant(withText("FullName: 42"))
                    )
                )
        }
    }

    @Test
    //метод, который нажимает на элемент списка:
    //Тут мы вызываем метод actionOnItemAtPosition, который в качестве аргументов принимает
    // позицию элемента, с которым мы хотим взаимодействовать, и Action. Если запустить тест,
    // то вы увидите всплывающее уведомление, позволяющее вам самостоятельно наблюдать
    // процесс исполнения теста.
    fun activitySearch_PerformClickAtPosition() {
        if (BuildConfig.TYPE == MainActivity.FAKE) {
            loadList()

            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                        0, click()
                    )
                )
        }
    }

    //усложним задачу и будем нажимать на элемент списка, который не виден на экране.
    // Для этого мы объединим функционал предыдущих методов: прокрутим список до нужного
    // элемента и нажмем на него
    @Test
    fun activitySearch_PerformClickOnItem() {
        if (BuildConfig.TYPE == MainActivity.FAKE) {
            loadList()

            //Обратите внимание, что мы проматываем чуть ниже. Это сделано для того,
            // чтобы искомый элемент был примерно посередине экрана.
            // Так работает промотка списка через метод scrollTo: искомый элемент
            // будет находиться в самом низу экрана, то есть будет самым последним в списке.
            // Промотка останавливается, как только элемент появляется на экране.
            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                        hasDescendant(withText("FullName: 50"))
                    )
                )

            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.actionOnItem<SearchResultAdapter.SearchResultViewHolder>(
                        hasDescendant(withText("FullName: 42")),
                        click()
                    )
                )
        }
    }

    @Test
    fun activitySearch_PerformCustomClick() {
        if (BuildConfig.TYPE == MainActivity.FAKE) {
            loadList()

            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions
                        .actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                            0,
                            tapOnItemWithId(R.id.checkbox)
                        )
                )
        }
    }

    private fun loadList() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
    }

    private fun tapOnItemWithId(id: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Нажимаем на view с указанным id"
        }

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById(id) as View
            v.performClick()
        }
    }

    //вставка текста может выглядеть так:
    //Достаточно найти view, в который нужно поместить текст и написать там переданную в метод строку.
    //!!У меня не работает
    private fun typeTextInChildViewWithId(id: Int, textToBeTyped: String): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Описание действия"
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id) as EditText
                v.setText(textToBeTyped)
            }
        }
    }



    @After
    fun close() {
        scenario.close()
    }
}