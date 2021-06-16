package com.geekbrains.tests.automator

import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)

//тест написан для телефона Xiaomi Redmi Note 4 - настройки находятся на главном экране. Язык - русский
class OpenOtherAppsTest {

    private val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    companion object {
        private const val TIMEOUT = 5000L
    }

    @Test
    fun test_OpenSettings() {
        uiDevice.pressHome()
        //Открываем экран со списком установленных приложений.
        //Обратите внимание, что на устройстве, для которого писался этот тест (Android_Emulator - Pixel_2_Q_10_api),
        //список приложений открывается свайпом снизу вверх на главном экране.
        //Метод swipe принимает координаты начальной и конечной точки свайпа.
        //В нашем случае это примерно снизу экрана строго вверх. Steps указывает, в
        //какое количество шагов мы хотим осуществить смахивание: чем выше число,
        //тем медленнее будет осуществляться свайп
        //uiDevice.swipe(500, 1500, 500, 0, 5)

        //Для других устройств список установленных приложений может открываться по другому.
        //Часто это иконка на главном экране под названием Apps.
        //Для этого достаточно свернуть все приложения через uiDevice.pressHome() и
        //и найти Apps на главном экране
        //val allAppsButton: UiObject = uiDevice.findObject(UiSelector().description("Apps"))
        //allAppsButton.clickAndWaitForNewWindow()
        //Вполне возможно (встречается на старых устройствах), что приложения находятся на вкладке Apps (будет еще вкладка Widgets).
        //Тогда еще найдем вкладку и выберем ее
        //val appsTab: UiObject = uiDevice.findObject(UiSelector().text("Apps"))
        //appsTab.click()

        //Приложений, обычно, установлено столько, что кнопка может быть за границей экрана
        //Тогда корневым контейнером будет Scrollable.
        //Если же все приложения умещаются на одном экране, то достаточно установить scrollable(false)
        val appViews = UiScrollable(UiSelector().scrollable(false))
        //Если прокрутка горизонтальная (встречается на старых устройствах), нужно установить
        // горизонтальную прокрутку (по умолчанию она вертикальная)
        //appViews.setAsHorizontalList()

        //Находим в контейнере настройки по названию иконки
        val settingsApp = appViews
            .getChildByText(
                UiSelector()
                    .className(TextView::class.java.name),
                "Настройки"
            )
        //Открываем
        settingsApp.clickAndWaitForNewWindow()

        //Убеждаемся, что Настройки открыты
        val settingsValidation =
            uiDevice.findObject(UiSelector().packageName("com.android.settings"))
        Assert.assertTrue(settingsValidation.exists())
    }

    @Test
    //заходим в настройки  Bluetooth
    fun test_OpenSettingsScreen() {
        uiDevice.pressHome()
        var appViews = UiScrollable(UiSelector().scrollable(false))
        //Находим в контейнере настройки по названию иконки
        val settingsApp = appViews
            .getChildByText(
                UiSelector()
                    .className(TextView::class.java.name),
                "Настройки"
            )
        //Открываем
        settingsApp.clickAndWaitForNewWindow()
        //разрешаем скролл
        appViews = UiScrollable(UiSelector().scrollable(true))
        //прокручиваем до Bluetooth
        appViews.scrollTextIntoView("Bluetooth" )
        //кликаем на Bluetooth
        uiDevice.findObject(By.text("Bluetooth")).click()
        //ждём появления текста Имя устройства
        uiDevice.wait(Until.hasObject(By.text("Имя устройства").depth(0)),
            TIMEOUT)
        //находим объект с текстом Имя устройства
       val bl =  uiDevice.findObject(By.text("Имя устройства"))
        //проверяем, что есть такой текст
        Assert.assertEquals( bl.text.toString(),"Имя устройства")
    }

}