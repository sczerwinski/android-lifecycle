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
import it.czerwinski.android.lifecycle.livedata.test.junit5.InstantTaskExecutorExtension
import it.czerwinski.android.lifecycle.livedata.test.test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("Tests for reducing LiveData")
class ReduceTest {

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with reduce, " +
            "WHEN posting values to source, " +
            "THEN reduced value should be observed after each emitted item"
    )
    fun reduce() {
        val source = MutableLiveData<String>()
        val observer = source.reduce { a, b -> "$a, $b" }.test()

        source.postValue("first")
        source.postValue("second")
        source.postValue(null)
        source.postValue("third")

        observer.assertValues(
            "first",
            "first, second",
            "first, second, null",
            "first, second, null, third"
        )
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with reduceNotNull, " +
            "WHEN posting values to source, " +
            "THEN reduced value should be observed after each emitted non-null item"
    )
    fun reduceNotNull() {
        val source = MutableLiveData<Int>()
        val observer = source.reduceNotNull { a, b -> a + b }.test()

        source.postValue(1)
        source.postValue(2)
        source.postValue(null)
        source.postValue(3)
        source.postValue(4)

        observer.assertValues(1, 3, 6, 10)
    }
}
