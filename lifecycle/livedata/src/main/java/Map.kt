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
 * Returns a [LiveData] emitting only the non-null results of applying the given [transform]
 * function to each value emitted by this LiveData.
 *
 * [transform] will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val userOptionLiveData: LiveData<Option<User>> = ...
 * val userLiveData: LiveData<User> = userOptionLiveData.mapNotNull { user -> user.getOrNull() }
 * ```
 */
fun <T, R> LiveData<T>.mapNotNull(
    transform: (T) -> R?
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) { x ->
        val y = transform(x)
        if (y != null) result.value = y
    }
    return result
}
