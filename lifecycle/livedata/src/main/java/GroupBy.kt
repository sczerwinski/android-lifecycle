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

/**
 * Returns a [GroupedLiveData] providing a set of [LiveData], each emitting a different subset of values from this
 * LiveData, based on the result of the given [keySelector] function.
 *
 * [keySelector] will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val userLiveData: LiveData<User> = ...
 * val userByStatusLiveData: GroupedLiveData<UserStatus, User> = errorLiveData.groupBy { user -> user.status }
 * val activeUserLiveData: LiveData<User> = userByStatusLiveData[UserStatus.ACTIVE]
 * ```
 */
fun <K, V> LiveData<V>.groupBy(keySelector: (V) -> K): GroupedLiveData<K, V> {
    val result = GroupedLiveData(keySelector)
    result.addSource(this, result::setValue)
    return result
}
