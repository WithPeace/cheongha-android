package com.withpeace.withpeace.benchmark

import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeBenchMark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()
}