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

package it.czerwinski.android.lifecycle.livedata.test.junit5

import androidx.lifecycle.liveData
import it.czerwinski.android.lifecycle.livedata.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class, TestCoroutineDispatcherExtension::class)
@DisplayName("Tests for TestCoroutineDispatcherExtension")
class TestCoroutineDispatcherExtensionTest {

    @Test
    @DisplayName(
        value = "GIVEN mocked observer for CoroutineLiveData, " +
            "WHEN assertValue, " +
            "THEN emitted values should be observed"
    )
    fun testWithTestCoroutineDispatcherExtension() {
        liveData { emit(1) }
            .test()
            .assertValue(1)
    }

    @Test
    @DisplayName(
        value = "GIVEN mocked observer for CoroutineLiveData, " +
                "WHEN advance time, " +
                "AND assertValues, " +
                "THEN emitted values should be observed at the current time"
    )
    fun testWithTestCoroutineScheduler(scheduler: TestCoroutineScheduler) {
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
