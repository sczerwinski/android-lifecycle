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

import it.czerwinski.android.lifecycle.livedata.test.junit5.InstantTaskExecutorExtension
import it.czerwinski.android.lifecycle.livedata.test.junit5.TestCoroutineDispatcherExtension
import it.czerwinski.android.lifecycle.livedata.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class, TestCoroutineDispatcherExtension::class)
@DisplayName("Tests for interval LiveData")
class IntervalTest {

    @ParameterizedTest(
        name = "GIVEN fixed interval of {0}ms, " +
                "WHEN advancing time by {1}ms, " +
                "THEN exactly {2} values should be observed"
    )
    @MethodSource("fixedIntervalTestData")
    fun fixedInterval(intervalInMillis: Long, elapsedTimeInMillis: Long, expectedValuesCount: Int) {
        val dispatcher = UnconfinedTestDispatcher()

        val observer = intervalLiveData(
            timeInMillis = intervalInMillis,
            context = dispatcher
        ).test()

        dispatcher.scheduler.apply { advanceTimeBy(delayTimeMillis = elapsedTimeInMillis); runCurrent() }

        observer.assertValues(0 until expectedValuesCount)
    }

    @ParameterizedTest(
        name = "GIVEN varying interval, " +
                "WHEN advancing time by {0}ms, " +
                "THEN exactly {1} values should be observed"
    )
    @MethodSource("varyingIntervalTestData")
    fun varyingInterval(elapsedTimeInMillis: Long, expectedValuesCount: Int) {
        val dispatcher = UnconfinedTestDispatcher()

        val observer = intervalLiveData(context = dispatcher) { index ->
            VARYING_INTERVAL_FACTOR * (index + 1)
        }.test()

        dispatcher.scheduler.apply { advanceTimeBy(delayTimeMillis = elapsedTimeInMillis); runCurrent() }

        observer.assertValues(0 until expectedValuesCount)
    }

    companion object {

        private const val VARYING_INTERVAL_FACTOR = 100L

        /**
         * Returns arguments for [fixedInterval] test method:
         * - `intervalInMillis`
         * - `elapsedTimeInMillis`
         * - `expectedValuesCount`
         */
        @JvmStatic
        fun fixedIntervalTestData(): List<Arguments> = listOf(
            Arguments.of(1000L, 900L, 0),
            Arguments.of(1000L, 1100L, 1),
            Arguments.of(1000L, 1900L, 1),
            Arguments.of(1000L, 2100L, 2),
            Arguments.of(1000L, 2900L, 2),
            Arguments.of(1000L, 3100L, 3),
            Arguments.of(100L, 1010L, 10),
        )

        /**
         * Returns arguments for [varyingInterval] test method:
         * - `elapsedTimeInMillis`
         * - `expectedValuesCount`
         */
        @JvmStatic
        fun varyingIntervalTestData(): List<Arguments> = listOf(
            Arguments.of(90L, 0),
            Arguments.of(110L, 1),
            Arguments.of(290L, 1),
            Arguments.of(310L, 2),
            Arguments.of(590L, 2),
            Arguments.of(610L, 3),
        )
    }
}
