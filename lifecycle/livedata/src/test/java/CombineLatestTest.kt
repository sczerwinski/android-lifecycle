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
@DisplayName("Tests for combining latest values of LiveData")
class CombineLatestTest {

    @Test
    @DisplayName(
        value = "GIVEN 2 combined source LiveData objects, " +
            "WHEN posting values to both sources, " +
            "THEN pairs of latest values from both sources should be observed"
    )
    fun combineLatest2() {
        val source1 = MutableLiveData<Int>()
        val source2 = MutableLiveData<Float>()

        val observer = combineLatest(source1, source2).test()

        source1.postValue(1)
        source2.postValue(1f)
        source1.postValue(2)
        source2.postValue(2f)

        observer.assertValues(
            1 to null,
            1 to 1f,
            2 to 1f,
            2 to 2f
        )
    }

    @Test
    @DisplayName(
        value = "GIVEN 3 combined source LiveData objects, " +
            "WHEN posting values to all sources, " +
            "THEN triples of latest values from all sources should be observed"
    )
    fun combineLatest3() {
        val source1 = MutableLiveData<Int>()
        val source2 = MutableLiveData<Float>()
        val source3 = MutableLiveData<String>()

        val observer = combineLatest(source1, source2, source3).test()

        source1.postValue(1)
        source2.postValue(1f)
        source3.postValue("text1")
        source1.postValue(2)
        source2.postValue(2f)
        source3.postValue("text2")

        observer.assertValues(
            Triple(first = 1, second = null, third = null),
            Triple(first = 1, second = 1f, third = null),
            Triple(first = 1, second = 1f, third = "text1"),
            Triple(first = 2, second = 1f, third = "text1"),
            Triple(first = 2, second = 2f, third = "text1"),
            Triple(first = 2, second = 2f, third = "text2")
        )
    }

    @Test
    @DisplayName(
        value = "GIVEN multiple combined source LiveData objects, " +
            "WHEN posting values to all sources, " +
            "THEN lists of latest values from all sources should be observed"
    )
    fun combineLatestList() {
        val source1 = MutableLiveData<String>()
        val source2 = MutableLiveData<String>()
        val source3 = MutableLiveData<String>()
        val source4 = MutableLiveData<String>()

        val observer = combineLatest(source1, source2, source3, source4).test()

        source3.postValue("World")
        source4.postValue("!")
        source1.postValue("Hello")
        source2.postValue(",")
        source3.postValue("Universe")
        source4.postValue(".")
        source1.postValue("Goodbye")
        source2.postValue(";")

        observer.assertValues(
            listOf(null, null, "World", null),
            listOf(null, null, "World", "!"),
            listOf("Hello", null, "World", "!"),
            listOf("Hello", ",", "World", "!"),
            listOf("Hello", ",", "Universe", "!"),
            listOf("Hello", ",", "Universe", "."),
            listOf("Goodbye", ",", "Universe", "."),
            listOf("Goodbye", ";", "Universe", ".")
        )
    }

    @Test
    @DisplayName(
        value = "GIVEN 2 combined (with function) source LiveData objects, " +
            "WHEN posting values to both sources, " +
            "THEN pairs of latest values from both sources should be observed"
    )
    fun combineLatest2WithFunction() {
        val source1 = MutableLiveData<Int>()
        val source2 = MutableLiveData<Float>()

        val observer = combineLatest(source1, source2) { a, b -> "$a, $b" }.test()

        source1.postValue(1)
        source2.postValue(1f)
        source1.postValue(2)
        source2.postValue(2f)

        observer.assertValues(
            "1, null",
            "1, 1.0",
            "2, 1.0",
            "2, 2.0"
        )
    }

    @Test
    @DisplayName(
        value = "GIVEN 3 combined (with function) source LiveData objects, " +
            "WHEN posting values to all sources, " +
            "THEN triples of latest values from all sources should be observed"
    )
    fun combineLatest3WithFunction() {
        val source1 = MutableLiveData<Int>()
        val source2 = MutableLiveData<Float>()
        val source3 = MutableLiveData<String>()

        val observer = combineLatest(source1, source2, source3) { a, b, c -> "$a, $b, $c" }.test()

        source1.postValue(1)
        source2.postValue(1f)
        source3.postValue("text1")
        source1.postValue(2)
        source2.postValue(2f)
        source3.postValue("text2")

        observer.assertValues(
            "1, null, null",
            "1, 1.0, null",
            "1, 1.0, text1",
            "2, 1.0, text1",
            "2, 2.0, text1",
            "2, 2.0, text2"
        )
    }

    @Test
    @DisplayName(
        value = "GIVEN multiple combined (with function) source LiveData objects, " +
            "WHEN posting values to all sources, " +
            "THEN lists of latest values from all sources should be observed"
    )
    fun combineLatestListWithFunction() {
        val source1 = MutableLiveData<String>()
        val source2 = MutableLiveData<String>()
        val source3 = MutableLiveData<String>()
        val source4 = MutableLiveData<String>()

        val observer = combineLatest(source1, source2, source3, source4) { values ->
            values.joinToString(separator = "")
        }.test()

        source3.postValue("World")
        source4.postValue("!")
        source1.postValue("Hello")
        source2.postValue(", ")
        source3.postValue("Universe")
        source4.postValue(".")
        source1.postValue("Goodbye")
        source2.postValue("; ")

        observer.assertValues(
            "nullnullWorldnull",
            "nullnullWorld!",
            "HellonullWorld!",
            "Hello, World!",
            "Hello, Universe!",
            "Hello, Universe.",
            "Goodbye, Universe.",
            "Goodbye; Universe."
        )
    }
}
