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

package it.czerwinski.android.lifecycle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import it.czerwinski.android.lifecycle.test.junit5.InstantTaskExecutorExtension
import it.czerwinski.android.lifecycle.test.junit5.TestCoroutineDispatcherExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class, TestCoroutineDispatcherExtension::class)
class ThrottleTest {

    @RelaxedMockK
    lateinit var intObserver: Observer<Int>

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with throttleWithTimeout, " +
            "WHEN posting multiple values to source, " +
            "THEN only the latest values after timeout should be observed"
    )
    fun throttleWithTimeout() {
        val dispatcher = TestCoroutineDispatcher()

        val source = MutableLiveData<Int>()
        val throttled = source.throttleWithTimeout(timeInMillis = 9_000L, context = dispatcher)
        throttled.observeForever(intObserver)

        source.postValue(1)
        dispatcher.advanceTimeBy(delayTimeMillis = 8_000L)
        source.postValue(2)
        dispatcher.advanceTimeBy(delayTimeMillis = 8_000L)
        source.postValue(3)
        dispatcher.advanceTimeBy(delayTimeMillis = 8_000L)
        source.postValue(4)
        dispatcher.advanceTimeBy(delayTimeMillis = 10_000L)
        source.postValue(5)
        dispatcher.advanceTimeBy(delayTimeMillis = 10_000L)
        source.postValue(6)
        dispatcher.advanceTimeBy(delayTimeMillis = 8_000L)
        source.postValue(7)
        dispatcher.advanceTimeBy(delayTimeMillis = 10_000L)

        verifySequence {
            intObserver.onChanged(4)
            intObserver.onChanged(5)
            intObserver.onChanged(7)
        }
    }
}
