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

import it.czerwinski.android.lifecycle.livedata.test.junit5.InstantTaskExecutorExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("Tests for TestObserver")
class TestObserverTest {

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged never called, " +
            "WHEN values, " +
            "THEN should return empty list"
    )
    fun valuesEmpty() {
        val observer = TestObserver.create<Int>()

        val result = observer.values()

        assertTrue(result.isEmpty())
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called multiple times, " +
            "WHEN values, " +
            "THEN should return ordered list of values"
    )
    fun valuesNonEmpty() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(3)

        val result = observer.values()

        assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged never called, " +
            "WHEN valueCount, " +
            "THEN should return 0"
    )
    fun valueCountZero() {
        val observer = TestObserver.create<Int>()

        val result = observer.valueCount()

        assertEquals(0, result)
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called multiple times, " +
            "WHEN valueCount, " +
            "THEN should return number of calls to onChanged"
    )
    fun valueCountMany() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(3)

        val result = observer.valueCount()

        assertEquals(3, result)
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged never called, " +
            "WHEN assertValue, " +
            "THEN should throw AssertionError"
    )
    fun assertValueNoValue() {
        val observer = TestObserver.create<Int>()

        assertThrows(AssertionError::class.java) {
            observer.assertValue(0)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called multiple times, " +
            "WHEN assertValue, " +
            "THEN should throw AssertionError"
    )
    fun assertValueManyValues() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(0)
        observer.onChanged(0)

        assertThrows(AssertionError::class.java) {
            observer.assertValue(0)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called once with wrong value, " +
            "WHEN assertValue, " +
            "THEN should throw AssertionError"
    )
    fun assertValueWrongValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)

        assertThrows(AssertionError::class.java) {
            observer.assertValue(0)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called once with correct value, " +
            "WHEN assertValue, " +
            "THEN should pass"
    )
    fun assertValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(0)

        observer.assertValue(0)
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged never called, " +
            "WHEN assertValue with predicate, " +
            "THEN should throw AssertionError"
    )
    fun assertValuePredicateNoValue() {
        val observer = TestObserver.create<Int>()

        assertThrows(AssertionError::class.java) {
            observer.assertValue { it == 0 }
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called multiple times, " +
            "WHEN assertValue with predicate, " +
            "THEN should throw AssertionError"
    )
    fun assertValuePredicateManyValues() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(0)
        observer.onChanged(0)

        assertThrows(AssertionError::class.java) {
            observer.assertValue { it == 0 }
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called once with wrong value, " +
            "WHEN assertValue with predicate, " +
            "THEN should throw AssertionError"
    )
    fun assertValuePredicateWrongValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)

        assertThrows(AssertionError::class.java) {
            observer.assertValue { it == 0 }
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called once with correct value, " +
            "WHEN assertValue with predicate, " +
            "THEN should pass"
    )
    fun assertValuePredicate() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(0)

        observer.assertValue { it == 0 }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged never called, " +
                "WHEN assertLatestValue, " +
                "THEN should throw AssertionError"
    )
    fun assertLatestValueNoValue() {
        val observer = TestObserver.create<Int>()

        assertThrows(AssertionError::class.java) {
            observer.assertLatestValue(0)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called with wrong latest value, " +
                "WHEN assertLatestValue, " +
                "THEN should throw AssertionError"
    )
    fun assertLatestValueWrongValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(0)
        observer.onChanged(1)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertLatestValue(0)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called with correct latest value, " +
                "WHEN assertLatestValue, " +
                "THEN should pass"
    )
    fun assertLatestValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(0)
        observer.onChanged(1)
        observer.onChanged(2)

        observer.assertLatestValue(2)
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged never called, " +
                "WHEN assertLatestValue with predicate, " +
                "THEN should throw AssertionError"
    )
    fun assertLatestValuePredicateNoValue() {
        val observer = TestObserver.create<Int>()

        assertThrows(AssertionError::class.java) {
            observer.assertLatestValue { it == 0 }
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called with wrong latest value, " +
                "WHEN assertLatestValue with predicate, " +
                "THEN should throw AssertionError"
    )
    fun assertLatestValuePredicateWrongValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(0)
        observer.onChanged(1)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertLatestValue { it == 0 }
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called with correct latest value, " +
                "WHEN assertLatestValue with predicate, " +
                "THEN should pass"
    )
    fun assertLatestValuePredicate() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(0)
        observer.onChanged(1)
        observer.onChanged(2)

        observer.assertLatestValue { it == 2 }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 2 times, " +
            "WHEN assertValues with 3 values, " +
            "THEN should throw AssertionError"
    )
    fun assertValuesTooFew() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertValues(1, 2, 3)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 4 times, " +
            "WHEN assertValues with 3 values, " +
            "THEN should throw AssertionError"
    )
    fun assertValuesTooMany() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(3)
        observer.onChanged(4)

        assertThrows(AssertionError::class.java) {
            observer.assertValues(1, 2, 3)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with a single wrong value, " +
            "WHEN assertValues with 3 values, " +
            "THEN should throw AssertionError"
    )
    fun assertValuesWrongValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(4)

        assertThrows(AssertionError::class.java) {
            observer.assertValues(1, 2, 3)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with correct values, " +
            "WHEN assertValues with 3 values, " +
            "THEN should pass"
    )
    fun assertValues() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(3)

        observer.assertValues(1, 2, 3)
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 2 times, " +
            "WHEN assertValues with iterable of 3 values, " +
            "THEN should throw AssertionError"
    )
    fun assertValuesWithIterableTooFew() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertValues(1..3)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 4 times, " +
            "WHEN assertValues with iterable of 3 values, " +
            "THEN should throw AssertionError"
    )
    fun assertValuesWithIterableTooMany() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(3)
        observer.onChanged(4)

        assertThrows(AssertionError::class.java) {
            observer.assertValues(1..3)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with a single wrong value, " +
            "WHEN assertValues with iterable of 3 values, " +
            "THEN should throw AssertionError"
    )
    fun assertValuesWithIterableWrongValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(4)

        assertThrows(AssertionError::class.java) {
            observer.assertValues(1..3)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with correct values, " +
            "WHEN assertValues with iterable of 3 values, " +
            "THEN should pass"
    )
    fun assertValuesWithIterable() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(3)

        observer.assertValues(1..3)
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 2 times, " +
            "WHEN assertValueSet with 3 values, " +
            "THEN should throw AssertionError"
    )
    fun assertValueSetTooFew() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertValueSet(listOf(1, 2, 3))
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 4 times, " +
            "WHEN assertValueSet with 3 values, " +
            "THEN should throw AssertionError"
    )
    fun assertValueSetTooMany() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(3)
        observer.onChanged(1)
        observer.onChanged(4)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertValueSet(listOf(1, 2, 3))
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with a single wrong value, " +
            "WHEN assertValueSet with 3 values, " +
            "THEN should throw AssertionError"
    )
    fun assertValueSetWrongValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(4)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertValueSet(listOf(1, 2, 3))
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with correct values, " +
            "WHEN assertValueSet with 3 values, " +
            "THEN should pass"
    )
    fun assertValueSet() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(3)
        observer.onChanged(1)
        observer.onChanged(2)

        observer.assertValueSet(listOf(1, 2, 3))
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 2 times, " +
            "WHEN assertValueAt index 2, " +
            "THEN should throw AssertionError"
    )
    fun assertValueAtTooFew() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertValueAt(index = 2, value = 3)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with wrong last value, " +
            "WHEN assertValueAt index 2, " +
            "THEN should throw AssertionError"
    )
    fun assertValueAtWrongValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertValueAt(index = 2, value = 3)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with correct values, " +
            "WHEN assertValueAt index 2, " +
            "THEN should pass"
    )
    fun assertValueAt() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(3)

        observer.assertValueAt(index = 2, value = 3)
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 2 times, " +
            "WHEN assertValueAt index 2 with predicate, " +
            "THEN should throw AssertionError"
    )
    fun assertValueAtWithPredicateTooFew() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertValueAt(index = 2) { it == 3 }
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with wrong last value, " +
            "WHEN assertValueAt index 2 with predicate, " +
            "THEN should throw AssertionError"
    )
    fun assertValueAtWithPredicateWrongValue() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(2)

        assertThrows(AssertionError::class.java) {
            observer.assertValueAt(index = 2) { it == 3 }
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times with correct values, " +
            "WHEN assertValueAt index 2 with predicate, " +
            "THEN should pass"
    )
    fun assertValueAtWithPredicate() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(3)

        observer.assertValueAt(index = 2) { it == 3 }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called once, " +
            "WHEN assertNoValues, " +
            "THEN should throw AssertionError"
    )
    fun assertNoValuesFail() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)

        assertThrows(AssertionError::class.java) {
            observer.assertNoValues()
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged never called, " +
            "WHEN assertNoValues, " +
            "THEN should pass"
    )
    fun assertNoValues() {
        val observer = TestObserver.create<Int>()

        observer.assertNoValues()
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 3 times, " +
            "WHEN assertValueCount 2, " +
            "THEN should throw AssertionError"
    )
    fun assertValueCountWrongNumber() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)
        observer.onChanged(3)

        assertThrows(AssertionError::class.java) {
            observer.assertValueCount(2)
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN TestObserver with onChanged called 2 times, " +
            "WHEN assertValueCount 2, " +
            "THEN should pass"
    )
    fun assertValueCount() {
        val observer = TestObserver.create<Int>()
        observer.onChanged(1)
        observer.onChanged(2)

        observer.assertValueCount(2)
    }
}
