import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.R
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
    //1 тест может не пройти, если изменится количество репозиториев на GitHub
    fun  activitySearch_IsWorking(){
        //Выделяем по тапу наш EditText
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText)).perform(ViewActions.click())
        //Вставляем в него текст запроса
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText))
            .perform(ViewActions.replaceText("algol"), ViewActions.closeSoftKeyboard())
        //Нажимаем на кнопку поиска на виртуальной клавиатуре
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText))
            .perform(ViewActions.pressImeActionButton())

        //“замораживаем” UI на 2 секунды.
        Espresso.onView(ViewMatchers.isRoot()).perform(delay())
        //Сравниваем отображаемый текст с ожидаемым результатом.
        Espresso.onView(ViewMatchers.withId(R.id.totalCountTextViewMain))
            .check(ViewAssertions.matches(ViewMatchers.withText("Number of results: 2457")))

    }

    //Будем возвращать свой собственный ViewAction, переопределив методы интерфейса,
    // который используется в методе perform():
    private fun delay(): ViewAction? {
        return object : ViewAction {
            //getConstraints() будет возвращать root-view нашей кнопки;
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
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