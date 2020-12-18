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
import androidx.lifecycle.Observer
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import it.czerwinski.android.lifecycle.livedata.test.junit5.InstantTaskExecutorExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
@DisplayName("Tests for filtering LiveData")
class FilterTest {

    @RelaxedMockK
    lateinit var intObserver: Observer<Int>

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with filter, " +
            "WHEN posting values to source, " +
            "THEN only values meeting the predicate should be observed"
    )
    fun filter() {
        val source = MutableLiveData<Int>()
        val filtered = source.filter { number -> number % 2 == 0 }
        filtered.observeForever(intObserver)

        source.postValue(1)
        source.postValue(2)
        source.postValue(3)
        source.postValue(4)

        verifySequence {
            intObserver.onChanged(2)
            intObserver.onChanged(4)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with filterNotNull, " +
            "WHEN posting values to source, " +
            "THEN only non-null values should be observed"
    )
    fun filterNotNull() {
        val source = MutableLiveData<Int?>()
        val filtered = source.filterNotNull()
        filtered.observeForever(intObserver)

        source.postValue(1)
        source.postValue(2)
        source.postValue(null)
        source.postValue(3)

        verifySequence {
            intObserver.onChanged(1)
            intObserver.onChanged(2)
            intObserver.onChanged(3)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with filterIsInstance, " +
            "WHEN posting values to source, " +
            "THEN only values of given type should be observed"
    )
    fun filterIsInstance() {
        val source = MutableLiveData<Any?>()
        val filtered = source.filterIsInstance<Int>()
        filtered.observeForever(intObserver)

        source.postValue(1)
        source.postValue("text")
        source.postValue(2)
        source.postValue(null)
        source.postValue(3)
        source.postValue("4")

        verifySequence {
            intObserver.onChanged(1)
            intObserver.onChanged(2)
            intObserver.onChanged(3)
        }
    }
}
