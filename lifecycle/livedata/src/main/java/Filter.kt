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
 * Returns a [LiveData] emitting only values from this LiveData matching the given [predicate].
 *
 * [predicate] will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val resultLiveData: LiveData<Try<User>> = ...
 * val successLiveData: LiveData<Try<User>> = resultLiveData.filter { it.isSuccess }
 * ```
 */
fun <T> LiveData<T>.filter(
    predicate: (T) -> Boolean
): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { x ->
        if (predicate(x)) result.value = x
    }
    return result
}

/**
 * Returns a [LiveData] emitting only non-null values from this LiveData.
 *
 * Null check will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val userLiveData: LiveData<User?> = ...
 * val nonNullUserLiveData: LiveData<User> = userLiveData.filterNotNull()
 * ```
 */
fun <T> LiveData<T?>.filterNotNull(): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { x ->
        if (x != null) result.value = x
    }
    return result
}

/**
 * Returns a [LiveData] emitting only values of type [R] from this LiveData.
 *
 * Type check will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val resultLiveData: LiveData<Try<User>> = ...
 * val failureLiveData: LiveData<Failure> = resultLiveData.filterIsInstance<Failure>()
 * ```
 */
inline fun <reified R> LiveData<*>.filterIsInstance(): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) { x ->
        if (x is R) result.value = x
    }
    return result
}
