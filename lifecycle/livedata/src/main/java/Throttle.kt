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

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Returns a [LiveData] emitting values from this LiveData, after dropping values followed by newer values before
 * [timeInMillis] expires.
 *
 * Delay will be applied on a thread determined by the given [context].
 *
 * **Example:**
 * ```
 * val isLoadingLiveData: LiveData<Boolean> = ...
 * val isLoadingThrottledLiveData: LiveData<Boolean> = isLoadingLiveData.throttleWithTimeout(
 *     timeInMillis = 1000L,
 *     context = viewModelScope.coroutineContext
 * )
 * ```
 */
fun <T> LiveData<T>.throttleWithTimeout(
    timeInMillis: Long,
    context: CoroutineContext = EmptyCoroutineContext
): LiveData<T> = switchMap { value ->
    liveData(context) {
        delay(timeInMillis)
        emit(value)
    }
}
