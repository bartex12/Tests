package com.geekbrains.tests

import android.content.Context
import android.os.Build
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.search.MainActivity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var context: Context

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
           assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
       assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityEditText_NotNull() {
        scenario.onActivity {
            val editText = it.findViewById<EditText>(R.id.searchEditText)
            editText.setText("one", TextView.BufferType.EDITABLE)
            assertNotNull(editText.text)
        }
    }

    @Test
    fun activityEditText_Equals() {
        scenario.onActivity {
            val editText = it.findViewById<EditText>(R.id.searchEditText)
            editText.setText("one", TextView.BufferType.EDITABLE)
            assertEquals("one", editText.text.toString())
        }
    }

    @Test
    fun activityEditText_TextEqualsAfterClick() {
    scenario.onActivity {
        val button = it.findViewById<Button>(R.id.toDetailsActivityButton)
        val editText = it.findViewById<EditText>(R.id.searchEditText)
        editText.setText("one", TextView.BufferType.EDITABLE)

        button.performClick()
        assertEquals("one", editText.text.toString())
    }
    }

}