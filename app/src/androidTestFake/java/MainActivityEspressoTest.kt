import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.R
import com.geekbrains.tests.view.search.MainActivity
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
       onView(ViewMatchers.withId(R.id.searchEditText)).perform(ViewActions.click())
        //Вставляем в него текст запроса
        onView(ViewMatchers.withId(R.id.searchEditText))
            .perform(ViewActions.replaceText("algol"), ViewActions.closeSoftKeyboard())
        //Нажимаем на кнопку поиска на виртуальной клавиатуре
        onView(ViewMatchers.withId(R.id.searchEditText))
            .perform(ViewActions.pressImeActionButton())

        onView(ViewMatchers.withId(R.id.totalCountTextViewMain))
            .check(ViewAssertions.matches(ViewMatchers.withText("Number of results: 42")))

    }

    @After
    fun close() {
        scenario.close()
    }
}