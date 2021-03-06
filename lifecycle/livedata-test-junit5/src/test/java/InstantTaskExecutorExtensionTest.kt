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

import androidx.lifecycle.MutableLiveData
import it.czerwinski.android.lifecycle.livedata.test.test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("Tests for InstantTaskExecutorExtension")
class InstantTaskExecutorExtensionTest {

    @Test
    @DisplayName(
        value = "GIVEN LiveData, " +
            "WHEN posting values to source, " +
            "THEN values should be observed"
    )
    fun testWithInstantTaskExecutorExtension() {
        val liveData = MutableLiveData<Int>()
        val observer = liveData.test()

        liveData.postValue(1)
        liveData.postValue(2)
        liveData.postValue(3)

        observer.assertValues(1, 2, 3)
    }
}
