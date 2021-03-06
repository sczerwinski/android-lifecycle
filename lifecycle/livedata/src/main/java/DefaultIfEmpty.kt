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

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns a [LiveData] that emits the values emitted by this LiveData or a specified default value if this LiveData has
 * not yet emitted any values at the time of observing.
 */
@SuppressLint("NullSafeMutableLiveData") // defaultValue can only be null if T is nullable
fun <T> LiveData<T>.defaultIfEmpty(defaultValue: T): LiveData<T> {
    val result = MediatorLiveData<T>()
    val observer = DistinguishedFirstTimeObserver<T>(
        firstTimeObserver = { x -> result.value = x },
        nextTimeObserver = { x -> result.value = x }
    )
    result.addSource(this, observer)
    result.addSource(ConstantLiveData(Unit)) { if (!observer.hasObserved) result.value = defaultValue }
    return result
}

/**
 * Returns a [LiveData] that emits the values emitted by this LiveData or a value returned by [defaultValueProducer] if
 * this LiveData has not yet emitted any values at the time of observing.
 *
 * [defaultValueProducer] will be executed on the main thread.
 */
fun <T> LiveData<T>.defaultIfEmpty(defaultValueProducer: () -> T): LiveData<T> {
    val result = MediatorLiveData<T>()
    val observer = DistinguishedFirstTimeObserver<T>(
        firstTimeObserver = { x -> result.value = x },
        nextTimeObserver = { x -> result.value = x }
    )
    result.addSource(this, observer)
    result.addSource(ConstantLiveData(Unit)) { if (!observer.hasObserved) result.value = defaultValueProducer() }
    return result
}
