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

package it.czerwinski.android.lifecycle.livedata

import androidx.lifecycle.MutableLiveData
import it.czerwinski.android.lifecycle.livedata.test.junit5.InstantTaskExecutorExtension
import it.czerwinski.android.lifecycle.livedata.test.junit5.TestCoroutineDispatcherExtension
import it.czerwinski.android.lifecycle.livedata.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class, TestCoroutineDispatcherExtension::class)
@DisplayName("Tests for throttling LiveData")
class ThrottleTest {

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with throttleWithTimeout, " +
            "WHEN posting multiple values to source, " +
            "THEN only the latest values after timeout should be observed"
    )
    fun throttleWithTimeout() {
        val dispatcher = UnconfinedTestDispatcher()

        val source = MutableLiveData<Int>()
        val observer =
            source.throttleWithTimeout(timeInMillis = 9_000L, context = dispatcher).test()

        source.postValue(1)
        dispatcher.scheduler.apply { advanceTimeBy(delayTimeMillis = 8_000L); runCurrent() }
        source.postValue(2)
        dispatcher.scheduler.apply { advanceTimeBy(delayTimeMillis = 8_000L); runCurrent() }
        source.postValue(3)
        dispatcher.scheduler.apply { advanceTimeBy(delayTimeMillis = 8_000L); runCurrent() }
        source.postValue(4)
        dispatcher.scheduler.apply { advanceTimeBy(delayTimeMillis = 10_000L); runCurrent() }
        source.postValue(5)
        dispatcher.scheduler.apply { advanceTimeBy(delayTimeMillis = 10_000L); runCurrent() }
        source.postValue(6)
        dispatcher.scheduler.apply { advanceTimeBy(delayTimeMillis = 8_000L); runCurrent() }
        source.postValue(7)
        dispatcher.scheduler.apply { advanceTimeBy(delayTimeMillis = 10_000L); runCurrent() }

        observer.assertValues(4, 5, 7)
    }
}
