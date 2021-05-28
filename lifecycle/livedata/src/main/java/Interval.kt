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
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Returns a [LiveData] emitting a sequence of integer values, spaced by a given [timeInMillis].
 *
 * Delay between subsequent emissions will be applied on a thread determined by the given [context].
 *
 * **Example:**
 * ```
 * val intervalLiveData: LiveData<Int> = intervalLiveData(timeInMillis = 1000L)
 * ```
 */
fun intervalLiveData(
    timeInMillis: Long,
    context: CoroutineContext = EmptyCoroutineContext
): LiveData<Int> = liveData(context) {
    var index = 0
    while (true) {
        delay(timeInMillis)
        emit(index++)
    }
}

/**
 * Returns a [LiveData] emitting a sequence of integer values, spaced by a result of a given
 * [timeInMillis] function invoked on the next value.
 *
 * Delay between subsequent emissions will be applied on a thread determined by the given [context].
 *
 * **Example:**
 * ```
 * val intervalLiveData: LiveData<Int> = intervalLiveData { index -> (index + 1) * 1000L }
 * ```
 */
fun intervalLiveData(
    context: CoroutineContext = EmptyCoroutineContext,
    timeInMillis: (Int) -> Long
): LiveData<Int> = liveData(context) {
    var index = 0
    while (true) {
        delay(timeInMillis(index))
        emit(index++)
    }
}
