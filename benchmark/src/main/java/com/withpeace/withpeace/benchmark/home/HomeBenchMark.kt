package com.withpeace.withpeace.benchmark.home

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeBenchMark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollPolicy() = benchmarkRule.measureRepeated(
        packageName = "com.withpeace.withpeace",
        metrics = listOf(FrameTimingMetric()),
        iterations = 10,
        startupMode = StartupMode.HOT,
        setupBlock = {
            pressHome()
            startActivityAndWait()
        }
    ) {
        val contentList = device.waitAndFindObject(By.res("home:policies"), 10000)
        contentList.setGestureMargin(device.displayWidth / 5)

        contentList.fling(Direction.DOWN)

        device.waitForIdle()
    }
}