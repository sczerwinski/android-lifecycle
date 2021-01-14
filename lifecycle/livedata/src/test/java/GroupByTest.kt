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
import it.czerwinski.android.lifecycle.livedata.test.TestObserver
import it.czerwinski.android.lifecycle.livedata.test.junit5.InstantTaskExecutorExtension
import it.czerwinski.android.lifecycle.livedata.test.test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("Tests for grouping emitted LiveData values")
class GroupByTest {

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with groupBy, " +
            "WHEN posting multiple values to source, " +
            "THEN emitted values should only be observed by the respective observer"
    )
    fun delayStart() {
        val source = MutableLiveData<Int>()
        val grouped = source.groupBy { number -> number > 0 }

        val combinedObserver = TestObserver.create<Int>()
        val positiveObserver = grouped[true].test(downstream = combinedObserver)
        val negativeObserver = grouped[false].test(downstream = combinedObserver)

        source.postValue(1)
        source.postValue(-1)
        source.postValue(-2)
        source.postValue(2)
        source.postValue(-3)
        source.postValue(3)
        source.postValue(4)
        source.postValue(-4)

        positiveObserver.assertValues(1, 2, 3, 4)
        negativeObserver.assertValues(-1, -2, -3, -4)
        combinedObserver.assertValues(1, -1, -2, 2, -3, 3, 4, -4)
    }
}
