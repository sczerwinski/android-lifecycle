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

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import it.czerwinski.android.lifecycle.livedata.test.junit5.InstantTaskExecutorExtension
import it.czerwinski.android.lifecycle.livedata.test.test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("Tests for MediatorLiveData extensions")
class MediatorLiveDataTest {

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

        val observer = mediatorLiveData.test()

        source.postValue(1)
        source.postValue(2)
        source.postValue(3)
        source.postValue(4)

        observer.assertValues(1, 2, 3, 4)
    }
}
