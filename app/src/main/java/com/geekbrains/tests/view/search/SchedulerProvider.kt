package com.geekbrains.tests.view.search

import io.reactivex.Scheduler

internal interface SchedulerProvider {
    fun ui(): Scheduler
    fun io(): Scheduler
}