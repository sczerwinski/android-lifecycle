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
@DisplayName("Tests for merging LiveData")
class MergeTest {

    @Test
    @DisplayName(
        value = "GIVEN 2 merged source LiveData objects, " +
            "WHEN posting values to both sources, " +
            "THEN values from both sources should be observed"
    )
    fun merge2() {
        val source1 = MutableLiveData<String>()
        val source2 = MutableLiveData<String>()

        val observer = (source1 merge source2).test()

        source1.postValue("text1")
        source2.postValue("text2")
        source1.postValue("text3")
        source2.postValue("text4")

        observer.assertValues(
            "text1",
            "text2",
            "text3",
            "text4"
        )
    }

    @Test
    @DisplayName(
        value = "GIVEN multiple merged source LiveData objects, " +
            "WHEN posting values to all sources, " +
            "THEN values from all sources should be observed"
    )
    fun mergeMany() {
        val source1 = MutableLiveData<String>()
        val source2 = MutableLiveData<String>()
        val source3 = MutableLiveData<String>()
        val source4 = MutableLiveData<String>()

        val observer = merge(source1, source2, source3, source4).test()

        source1.postValue("text1")
        source2.postValue("text2")
        source3.postValue("text3")
        source4.postValue("text4")
        source1.postValue("text5")
        source2.postValue("text6")
        source3.postValue("text7")
        source4.postValue("text8")

        observer.assertValues(
            "text1",
            "text2",
            "text3",
            "text4",
            "text5",
            "text6",
            "text7",
            "text8"
        )
    }
}
