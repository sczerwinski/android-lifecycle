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
@DisplayName("Tests for combining latest values of LiveData")
class CombineLatestTest {

    @RelaxedMockK
    lateinit var pairObserver: Observer<Pair<Int?, Float?>?>

    @RelaxedMockK
    lateinit var tripleObserver: Observer<Triple<Int?, Float?, String?>?>

    @RelaxedMockK
    lateinit var listObserver: Observer<List<String?>?>

    @RelaxedMockK
    lateinit var stringObserver: Observer<String?>

    @Test
    @DisplayName(
        value = "GIVEN 2 combined source LiveData objects, " +
            "WHEN posting values to both sources, " +
            "THEN pairs of latest values from both sources should be observed"
    )
    fun combineLatest2() {
        val source1 = MutableLiveData<Int>()
        val source2 = MutableLiveData<Float>()

        val combined = combineLatest(source1, source2)
        combined.observeForever(pairObserver)

        source1.postValue(1)
        source2.postValue(1f)
        source1.postValue(2)
        source2.postValue(2f)

        verifySequence {
            pairObserver.onChanged(1 to null)
            pairObserver.onChanged(1 to 1f)
            pairObserver.onChanged(2 to 1f)
            pairObserver.onChanged(2 to 2f)
        }
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

        val combined = combineLatest(source1, source2, source3)
        combined.observeForever(tripleObserver)

        source1.postValue(1)
        source2.postValue(1f)
        source3.postValue("text1")
        source1.postValue(2)
        source2.postValue(2f)
        source3.postValue("text2")

        verifySequence {
            tripleObserver.onChanged(Triple(first = 1, second = null, third = null))
            tripleObserver.onChanged(Triple(first = 1, second = 1f, third = null))
            tripleObserver.onChanged(Triple(first = 1, second = 1f, third = "text1"))
            tripleObserver.onChanged(Triple(first = 2, second = 1f, third = "text1"))
            tripleObserver.onChanged(Triple(first = 2, second = 2f, third = "text1"))
            tripleObserver.onChanged(Triple(first = 2, second = 2f, third = "text2"))
        }
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

        val combined = combineLatest(source1, source2, source3, source4)
        combined.observeForever(listObserver)

        source3.postValue("World")
        source4.postValue("!")
        source1.postValue("Hello")
        source2.postValue(",")
        source3.postValue("Universe")
        source4.postValue(".")
        source1.postValue("Goodbye")
        source2.postValue(";")

        verifySequence {
            listObserver.onChanged(listOf(null, null, "World", null))
            listObserver.onChanged(listOf(null, null, "World", "!"))
            listObserver.onChanged(listOf("Hello", null, "World", "!"))
            listObserver.onChanged(listOf("Hello", ",", "World", "!"))
            listObserver.onChanged(listOf("Hello", ",", "Universe", "!"))
            listObserver.onChanged(listOf("Hello", ",", "Universe", "."))
            listObserver.onChanged(listOf("Goodbye", ",", "Universe", "."))
            listObserver.onChanged(listOf("Goodbye", ";", "Universe", "."))
        }
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

        val combined = combineLatest(source1, source2) { a, b -> "$a, $b" }
        combined.observeForever(stringObserver)

        source1.postValue(1)
        source2.postValue(1f)
        source1.postValue(2)
        source2.postValue(2f)

        verifySequence {
            stringObserver.onChanged("1, null")
            stringObserver.onChanged("1, 1.0")
            stringObserver.onChanged("2, 1.0")
            stringObserver.onChanged("2, 2.0")
        }
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

        val combined = combineLatest(source1, source2, source3) { a, b, c -> "$a, $b, $c" }
        combined.observeForever(stringObserver)

        source1.postValue(1)
        source2.postValue(1f)
        source3.postValue("text1")
        source1.postValue(2)
        source2.postValue(2f)
        source3.postValue("text2")

        verifySequence {
            stringObserver.onChanged("1, null, null")
            stringObserver.onChanged("1, 1.0, null")
            stringObserver.onChanged("1, 1.0, text1")
            stringObserver.onChanged("2, 1.0, text1")
            stringObserver.onChanged("2, 2.0, text1")
            stringObserver.onChanged("2, 2.0, text2")
        }
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

        val combined = combineLatest(source1, source2, source3, source4) { values ->
            values.joinToString(separator = "")
        }
        combined.observeForever(stringObserver)

        source3.postValue("World")
        source4.postValue("!")
        source1.postValue("Hello")
        source2.postValue(", ")
        source3.postValue("Universe")
        source4.postValue(".")
        source1.postValue("Goodbye")
        source2.postValue("; ")

        verifySequence {
            stringObserver.onChanged("nullnullWorldnull")
            stringObserver.onChanged("nullnullWorld!")
            stringObserver.onChanged("HellonullWorld!")
            stringObserver.onChanged("Hello, World!")
            stringObserver.onChanged("Hello, Universe!")
            stringObserver.onChanged("Hello, Universe.")
            stringObserver.onChanged("Goodbye, Universe.")
            stringObserver.onChanged("Goodbye; Universe.")
        }
    }
}
