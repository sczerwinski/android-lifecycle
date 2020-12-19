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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoTestCoroutineDispatcherRuleTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var intObserver: Observer<Int>

    @Before
    fun setUpMocks() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test(expected = Exception::class)
    fun testWithoutTestCoroutineDispatcherRule() {
        val liveData = liveData {
            emit(1)
        }

        liveData.observeForever(intObserver)

        verifySequence {
            intObserver.onChanged(1)
        }
    }
}
