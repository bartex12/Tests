package com.geekbrains.tests.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class DetailsActivityAutomatorTests {
    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
    private val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()
    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName

    companion object {
        private const val TIMEOUT = 5000L
    }

    @Before
    fun setup() {
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()

        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        //Мы уже проверяли Интент на null в предыдущем тесте, поэтому допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)//Чистим бэкстек от запущенных ранее Активити
        context.startActivity(intent)

        //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    @Test
    //Убеждаемся, в DetailsScreen срабатывает кнопка назад и возвращаемся в Main
    fun test_DetailsScreenBackUp() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(By.text( "TO DETAILS" ))
        //Кликаем по ней
        if (toDetails.isEnabled){
            toDetails.click()
        }
        //ждём TIMEOUT
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
       //кликаем кнопку Назад
        uiDevice.pressBack()
        //Ждем TIMEOUT  пока приложение переключается на MainActivity
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
        //проверяем что переключилось , по соответствию надписи на кнопке
        val toDetails2: UiObject2 = uiDevice.findObject(By.text( "TO DETAILS" ))
        Assert.assertEquals(toDetails2.text.toString(), "TO DETAILS")
    }

    @Test
    //Проверим как у нас отрабатываются нажатия на кнопки
    fun activityButtonDecrement_IsWorking() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(packageName, "toDetailsActivityButton"))
        //Кликаем по ней
        toDetails.click()
        //ждём TIMEOUT
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
        //Находим кнопку
        val decrementButton: UiObject2 = uiDevice.findObject(
            By.res(packageName, "decrementButton"))
        decrementButton.click()
        val changedText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        Assert.assertEquals(changedText.text, "Number of results: -1")
    }

    @Test
    //Проверим как у нас отрабатываются нажатия на кнопки
    fun activityButtonIncrement_IsWorking() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(packageName, "toDetailsActivityButton"))
        //Кликаем по ней
        toDetails.click()
        //ждём TIMEOUT
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
        //Находим кнопку
        val incrementButton: UiObject2 = uiDevice.findObject(
            By.res(packageName, "incrementButton"))
        incrementButton.click()
        val changedText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        Assert.assertEquals(changedText.text, "Number of results: 1")
    }

}