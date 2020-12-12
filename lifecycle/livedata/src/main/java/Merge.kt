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
 * Returns a [LiveData] emitting each value emitted by either this or [other] LiveData.
 *
 * **Example:**
 * ```
 * val serverError: LiveData<String> = ...
 * val databaseError: LiveData<String> = ...
 * val error: LiveData<String> = serverError merge databaseError
 * ```
 */
infix fun <T> LiveData<T>.merge(other: LiveData<T>): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { x -> result.value = x }
    result.addSource(other) { x -> result.value = x }
    return result
}

/**
 * Returns a [LiveData] emitting each value emitted by any of the given LiveData.
 *
 * **Example:**
 * ```
 * val serverError: LiveData<String> = ...
 * val databaseError: LiveData<String> = ...
 * val fileError: LiveData<String> = ...
 * val error: LiveData<String> = merge(serverError, databaseError, fileError)
 * ```
 */
fun <T> merge(vararg sources: LiveData<T>): LiveData<T> {
    val result = MediatorLiveData<T>()
    for (source in sources) {
        result.addSource(source) { x -> result.value = x }
    }
    return result
}
