package com.geekbrains.tests.viewmodel

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun ui(): Scheduler
    fun io(): Scheduler
}