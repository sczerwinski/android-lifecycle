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

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Starts to listen the given [source] LiveData.
 * Whenever [source] value is changed, it is set as a new value of this [MediatorLiveData].
 *
 * If the given LiveData is already added as a source [IllegalArgumentException] will be thrown.
 *
 * Equivalent to:
 * ```
 * mediatorLiveData.addSource(liveData) { x -> mediatorLiveData.value = x }
 * ```
 */
@Suppress("NOTHING_TO_INLINE")
@MainThread
inline fun <T> MediatorLiveData<in T>.addDirectSource(source: LiveData<out T>) {
    addSource(source) { x -> value = x }
}
