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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.czerwinski.android.lifecycle.livedata.test.junit5.InstantTaskExecutorExtension
import it.czerwinski.android.lifecycle.livedata.test.test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("Tests for switching LiveData")
class SwitchTest {

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with switch, " +
            "WHEN posting new LiveData to source, " +
            "THEN values from the most recently posted LiveData should be observed"
    )
    fun reduce() {
        val source = MutableLiveData<LiveData<Int>>()
        val observer = source.switch().test()

        val liveData1 = MutableLiveData<Int>()
        val liveData2 = MutableLiveData<Int>()

        source.postValue(liveData1)
        liveData1.postValue(1)
        liveData1.postValue(2)
        liveData1.postValue(3)
        source.postValue(liveData2)
        liveData1.postValue(4)
        liveData2.postValue(10)
        liveData2.postValue(20)
        liveData1.postValue(5)
        liveData2.postValue(30)
        liveData2.postValue(40)

        observer.assertValues(1, 2, 3, 10, 20, 30, 40)
    }
}
