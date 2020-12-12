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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns a [LiveData] emitting non-null accumulated value starting with the first non-null value
 * emitted by this LiveData and applying [operation] from left to right to current accumulator value
 * and each subsequent non-null value emitted by this LiveData.
 *
 * [operation] will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val newOperationsCountLiveData: LiveData<Int> = ...
 * val operationsCountLiveData: LiveData<Int> =
 *     newOperationsCountLiveData.reduceNotNull { acc, next -> acc + next }
 * ```
 */
fun <T> LiveData<T>.reduceNotNull(
    operation: (T, T) -> T
): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { x ->
        if (x != null) {
            val oldValue = result.value
            result.value =
                if (oldValue == null) x
                else operation(oldValue, x)
        }
    }
    return result
}

/**
 * Returns a [LiveData] emitting accumulated value starting with the first value emitted by this
 * LiveData and applying [operation] from left to right to current accumulator value and each value
 * emitted by this.
 *
 * [operation] will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val newOperationsCountLiveData: LiveData<Int?> = ...
 * val operationsCountLiveData: LiveData<Int?> =
 *     newOperationsCountLiveData.reduce { acc, next -> if (next == null) null else acc + next }
 * ```
 */
fun <T> LiveData<T?>.reduce(
    operation: (T?, T?) -> T?
): LiveData<T?> {
    val result = MediatorLiveData<T>()
    result.addSource(this, DistinguishedFirstTimeObserver(
        firstTimeObserver = { x -> result.value = x },
        nextTimeObserver = { x -> result.value = operation(result.value, x) }
    ))
    return result
}
