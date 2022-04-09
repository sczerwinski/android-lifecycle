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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit4 test rule that swaps main coroutine dispatcher with [UnconfinedTestDispatcher].
 */
@ExperimentalCoroutinesApi
class TestCoroutineDispatcherRule : TestWatcher() {

    private lateinit var _scheduler: TestCoroutineScheduler

    /**
     * Test coroutine scheduler.
     */
    val scheduler: TestCoroutineScheduler get() = _scheduler

    override fun starting(description: Description?) {
        super.starting(description)
        _scheduler = TestCoroutineScheduler()
        Dispatchers.setMain(UnconfinedTestDispatcher(_scheduler))
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
