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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver

/**
 * JUnit5 extension that swaps main coroutine dispatcher with an [UnconfinedTestDispatcher].
 *
 * Values of method parameters of type [TestCoroutineScheduler] will be resolved as
 * the scheduler of the [UnconfinedTestDispatcher].
 *
 * Example:
 * ```
 * @ExtendWith(TestCoroutineDispatcherExtension::class)
 * class MyTestClass {
 *
 *     @Test
 *     fun someTest(scheduler: TestCoroutineScheduler) {
 *         // ...
 *         scheduler.advanceTimeBy(delayTimeMillis = 1000L)
 *         scheduler.runCurrent()
 *         // ...
 *     }
 * }
 * ```
 */
@ExperimentalCoroutinesApi
class TestCoroutineDispatcherExtension : TypeBasedParameterResolver<TestCoroutineScheduler>(),
    BeforeEachCallback,
    AfterEachCallback {

    private val scheduler: TestCoroutineScheduler = TestCoroutineScheduler()

    override fun resolveParameter(
        parameterContext: ParameterContext?,
        extensionContext: ExtensionContext?
    ): TestCoroutineScheduler = scheduler

    override fun beforeEach(context: ExtensionContext) {
        Dispatchers.setMain(UnconfinedTestDispatcher(scheduler))
    }

    override fun afterEach(context: ExtensionContext) {
        Dispatchers.resetMain()
    }
}
