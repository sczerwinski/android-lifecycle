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
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import it.czerwinski.android.lifecycle.test.junit5.InstantTaskExecutorExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
@DisplayName("Tests for grouping emitted LiveData values")
class GroupByTest {

    @RelaxedMockK
    lateinit var positiveIntObserver: Observer<Int>

    @RelaxedMockK
    lateinit var negativeIntObserver: Observer<Int>

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with groupBy, " +
            "WHEN posting multiple values to source, " +
            "THEN emitted values should only be observed by the respective observer"
    )
    fun delayStart() {
        val source = MutableLiveData<Int>()
        val grouped = source.groupBy { number -> number > 0 }
        grouped[true].observeForever(positiveIntObserver)
        grouped[false].observeForever(negativeIntObserver)

        source.postValue(1)
        source.postValue(-1)
        source.postValue(-2)
        source.postValue(2)
        source.postValue(-3)
        source.postValue(3)
        source.postValue(4)
        source.postValue(-4)

        verifySequence {
            positiveIntObserver.onChanged(1)
            negativeIntObserver.onChanged(-1)
            negativeIntObserver.onChanged(-2)
            positiveIntObserver.onChanged(2)
            negativeIntObserver.onChanged(-3)
            positiveIntObserver.onChanged(3)
            positiveIntObserver.onChanged(4)
            negativeIntObserver.onChanged(-4)
        }
        confirmVerified(positiveIntObserver, negativeIntObserver)
    }
}