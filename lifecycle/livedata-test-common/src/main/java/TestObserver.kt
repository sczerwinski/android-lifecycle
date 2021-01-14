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

package it.czerwinski.android.lifecycle.livedata.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * A callback testing values emitted by [LiveData].
 *
 * @since 1.1.0
 */
@Suppress("TooManyFunctions")
class TestObserver<T> internal constructor(
    private val downstream: Observer<T>? = null
) : Observer<T> {

    private val values = mutableListOf<T>()

    /**
     * Called when the observed [LiveData] emits a new value.
     */
    override fun onChanged(t: T) {
        values.add(t)
        downstream?.onChanged(t)
    }

    /**
     * Returns a list of received [onChanged] values.
     */
    fun values(): List<T> = values.toList()

    /**
     * Returns the number of received [onChanged] values.
     */
    fun valueCount(): Int = values.size

    /**
     * Asserts that this observer received exactly one [onChanged] value,
     * equal to the given [value].
     */
    fun assertValue(value: T): TestObserver<T> {
        assertValues(listOf(value), expectedMessage = "exactly 1 value")
        return this
    }

    private fun assertValues(expectedValues: List<T>, expectedMessage: String) {
        if (values != expectedValues) {
            fail(
                summary = "Values do not match",
                expectedMessage = expectedMessage,
                expectedValues = expectedValues,
                actualMessage = if (values.isEmpty()) "no values" else "exactly ${values.size.toValuesCount()}",
                actualValues = values
            )
        }
    }

    private fun Int.toValuesCount(): String =
        if (this == 1) "1 value"
        else "$this values"

    private fun fail(
        summary: String,
        expectedMessage: String? = null,
        expectedValues: List<T>? = null,
        actualMessage: String? = null,
        actualValues: List<T>? = null
    ) {
        throw AssertionError(
            listOfNotNull(
                "$summary.",
                expectedMessage?.let { "Expected: $it" },
                expectedValues?.toEnumeration(),
                actualMessage?.let { "Observed: $it" },
                actualValues?.toEnumeration()
            ).joinToString(separator = "\n")
        )
    }

    private fun List<T>.toEnumeration(): String = joinToString(separator = "\n") { value ->
        " * $value (${(value as? Any)?.javaClass})"
    }

    /**
     * Asserts that this observer received exactly one [onChanged] value,
     * for which the given [predicate] returns `true`.
     */
    fun assertValue(predicate: (T) -> Boolean): TestObserver<T> {
        if (values.size != 1) {
            fail(
                summary = "Value count does not match",
                expectedMessage = "exactly 1 value matching the predicate",
                actualMessage = "exactly ${values.size.toValuesCount()}",
                actualValues = values
            )
        }
        if (!predicate(values.first())) {
            fail(
                summary = "Value does not match the predicate",
                actualMessage = "exactly 1 value",
                actualValues = values
            )
        }
        return this
    }

    /**
     * Asserts that this observer received only the specified [values] in the exact specified order.
     */
    fun assertValues(vararg values: T): TestObserver<T> {
        assertValues(values.toList(), expectedMessage = "values in exact order")
        return this
    }

    /**
     * Asserts that this observer received only the specified [values] in the exact specified order.
     */
    fun assertValues(values: Iterable<T>): TestObserver<T> {
        assertValues(values.toList(), expectedMessage = "values in exact order")
        return this
    }

    /**
     * Asserts that this observer received only the specified [values], irrespective of the order.
     */
    fun assertValueSet(values: Collection<T>): TestObserver<T> {
        if (this.values.size != values.size) {
            fail(
                summary = "Value count does not match",
                expectedMessage = "exactly ${values.size.toValuesCount()} in any order",
                expectedValues = values.toList(),
                actualMessage = "exactly ${this.values.size.toValuesCount()}",
                actualValues = this.values
            )
        }
        for ((index, value) in this.values.withIndex()) {
            if (value !in values) {
                fail(
                    summary = "Values do not match",
                    actualMessage = "unexpected value at index $index",
                    actualValues = listOf(value)
                )
            }
        }
        return this
    }

    /**
     * Asserts that this observer received an [onChanged] value at the given [index],
     * equal to the given [value].
     */
    fun assertValueAt(index: Int, value: T): TestObserver<T> {
        if (index >= values.size) {
            fail(
                summary = "Value count does not match",
                expectedMessage = "value at index $index",
                expectedValues = listOf(value),
                actualMessage = "only ${this.values.size.toValuesCount()}",
                actualValues = this.values
            )
        }
        if (values[index] != value) {
            fail(
                summary = "Values do not match",
                expectedMessage = "value at index $index",
                expectedValues = listOf(value),
                actualMessage = "value at index $index",
                actualValues = listOf(values[index])
            )
        }
        return this
    }

    /**
     * Asserts that this observer received an [onChanged] value at the given [index],
     * for which the given [predicate] returns `true`.
     */
    fun assertValueAt(index: Int, predicate: (T) -> Boolean): TestObserver<T> {
        if (index >= values.size) {
            fail(
                summary = "Value count does not match",
                expectedMessage = "value at index $index matching the predicate",
                actualMessage = "only ${this.values.size.toValuesCount()}",
                actualValues = this.values
            )
        }
        if (!predicate(values[index])) {
            fail(
                summary = "Values do not match",
                expectedMessage = "value at index $index matching the predicate",
                actualMessage = "value at index $index",
                actualValues = listOf(values[index])
            )
        }
        return this
    }

    /**
     * Assert that this observer received no [onChanged] events.
     */
    fun assertNoValues(): TestObserver<T> {
        if (values.isNotEmpty()) {
            fail(
                summary = "Value count does not match",
                expectedMessage = "no values",
                actualMessage = "exactly ${values.size.toValuesCount()}",
                actualValues = values
            )
        }
        return this
    }

    /**
     * Assert that this observer received the specified number [onChanged] events.
     */
    fun assertValueCount(count: Int): TestObserver<T> {
        if (values.size != count) {
            fail(
                summary = "Value count does not match",
                expectedMessage = "exactly ${count.toValuesCount()}",
                actualMessage = "exactly ${values.size.toValuesCount()}",
                actualValues = values
            )
        }
        return this
    }

    companion object {

        /**
         * Returns a new non-forwarding [TestObserver].
         */
        fun <T> create(): TestObserver<T> = TestObserver()

        /**
         * Returns a new forwarding [TestObserver].
         */
        fun <T> create(downstream: Observer<T>): TestObserver<T> = TestObserver(downstream)
    }
}
