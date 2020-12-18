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
@DisplayName("Tests for setting default value to empty LiveData")
class DefaultIfEmptyTest {

    @RelaxedMockK
    lateinit var stringObserver: Observer<String?>

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with applied default value, " +
            "WHEN observing the LiveData before any value is emitted, " +
            "THEN default value should be observed"
    )
    fun defaultIfEmptyObservedBeforeEmitted() {
        val source = MutableLiveData<String>()
        val transformed = source.defaultIfEmpty(defaultValue = "default value")

        transformed.observeForever(stringObserver)

        source.postValue("real value 1")
        source.postValue("real value 2")

        verifySequence {
            stringObserver.onChanged("default value")
            stringObserver.onChanged("real value 1")
            stringObserver.onChanged("real value 2")
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with applied default value, " +
            "WHEN observing the LiveData after a value is emitted, " +
            "THEN default value should not be observed"
    )
    fun defaultIfEmptyObservedAfterEmitted() {
        val source = MutableLiveData<String>()
        val transformed = source.defaultIfEmpty(defaultValue = "default value")

        source.postValue("real value 1")

        transformed.observeForever(stringObserver)

        source.postValue("real value 2")

        verifySequence {
            stringObserver.onChanged("real value 1")
            stringObserver.onChanged("real value 2")
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with applied default value, " +
            "WHEN observing the LiveData after null is emitted, " +
            "THEN default value should not be observed"
    )
    fun defaultIfEmptyObservedAfterNullEmitted() {
        val source = MutableLiveData<String?>()
        val transformed = source.defaultIfEmpty(defaultValue = "default value")

        source.postValue(null)

        transformed.observeForever(stringObserver)

        source.postValue("real value 2")

        verifySequence {
            stringObserver.onChanged(null)
            stringObserver.onChanged("real value 2")
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with applied default value after a value is emitted, " +
            "WHEN observing the LiveData, " +
            "THEN default value should not be observed"
    )
    fun defaultIfEmptyAfterEmitted() {
        val source = MutableLiveData<String>()

        source.postValue("real value 1")

        val transformed = source.defaultIfEmpty(defaultValue = "default value")
        transformed.observeForever(stringObserver)

        source.postValue("real value 2")

        verifySequence {
            stringObserver.onChanged("real value 1")
            stringObserver.onChanged("real value 2")
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with applied default value producer, " +
            "WHEN observing the LiveData before any value is emitted, " +
            "THEN default value should be observed"
    )
    fun defaultIfEmptyWithProducerObservedBeforeEmitted() {
        val source = MutableLiveData<String>()
        val transformed = source.defaultIfEmpty<String> { "default value" }

        transformed.observeForever(stringObserver)

        source.postValue("real value 1")
        source.postValue("real value 2")

        verifySequence {
            stringObserver.onChanged("default value")
            stringObserver.onChanged("real value 1")
            stringObserver.onChanged("real value 2")
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with applied default value producer, " +
            "WHEN observing the LiveData after a value is emitted, " +
            "THEN default value producer should never be called"
    )
    fun defaultIfEmptyWithProducerObservedAfterEmitted() {
        val source = MutableLiveData<String>()
        val transformed = source.defaultIfEmpty<String> { throw AssertionError("Producer called") }

        source.postValue("real value 1")

        transformed.observeForever(stringObserver)

        source.postValue("real value 2")

        verifySequence {
            stringObserver.onChanged("real value 1")
            stringObserver.onChanged("real value 2")
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with applied default value producer, " +
            "WHEN observing the LiveData after null is emitted, " +
            "THEN default value producer should never be called"
    )
    fun defaultIfEmptyWithProducerObservedAfterNullEmitted() {
        val source = MutableLiveData<String?>()
        val transformed = source.defaultIfEmpty<String?> { throw AssertionError("Producer called") }

        source.postValue(null)

        transformed.observeForever(stringObserver)

        source.postValue("real value 2")

        verifySequence {
            stringObserver.onChanged(null)
            stringObserver.onChanged("real value 2")
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with applied default value producer after a value is emitted, " +
            "WHEN observing the LiveData, " +
            "THEN default value producer should never be called"
    )
    fun defaultIfEmptyWithProducerAfterEmitted() {
        val source = MutableLiveData<String>()

        source.postValue("real value 1")

        val transformed = source.defaultIfEmpty<String> { throw AssertionError("Producer called") }
        transformed.observeForever(stringObserver)

        source.postValue("real value 2")

        verifySequence {
            stringObserver.onChanged("real value 1")
            stringObserver.onChanged("real value 2")
        }
    }
}
