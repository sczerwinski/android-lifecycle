/*
 * Copyright 2020 Slawomir Czerwinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package it.czerwinski.android.lifecycle.livedata.test.junit4

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import it.czerwinski.android.lifecycle.livedata.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TestCoroutineDispatcherRuleTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testCoroutineDispatcherRule = TestCoroutineDispatcherRule()

    private val scheduler get() = testCoroutineDispatcherRule.scheduler

    @Test
    fun testWithTestCoroutineDispatcherRule() {
        liveData { emit(1) }
            .test()
            .assertValue(1)
    }

    @Test
    fun testWithTestCoroutineScheduler() {
        val observer = liveData {
            emit(1)
            delay(1000L)
            emit(2)
            delay(1000L)
            emit(3)
        }.test()

        scheduler.advanceTimeBy(1500L)
        scheduler.runCurrent()

        observer.assertValues(1, 2)
    }
}
