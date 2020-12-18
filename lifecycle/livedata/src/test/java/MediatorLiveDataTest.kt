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

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import it.czerwinski.android.lifecycle.test.junit5.InstantTaskExecutorExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
@DisplayName("Tests for MediatorLiveData extensions")
class MediatorLiveDataTest {

    @RelaxedMockK
    lateinit var intObserver: Observer<Int?>

    @Test
    @DisplayName(
        value = "GIVEN LiveData added as a direct source to MediatorLiveData, " +
            "WHEN posting values to source, " +
            "THEN all emitted values should be observed"
    )
    fun mapNotNull() {
        val source = MutableLiveData<Int>()
        val mediatorLiveData = MediatorLiveData<Int>()
        mediatorLiveData.addDirectSource(source)
        mediatorLiveData.observeForever(intObserver)

        source.postValue(1)
        source.postValue(2)
        source.postValue(3)
        source.postValue(4)

        verifySequence {
            intObserver.onChanged(1)
            intObserver.onChanged(2)
            intObserver.onChanged(3)
            intObserver.onChanged(4)
        }
    }
}