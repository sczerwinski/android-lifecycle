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
import androidx.lifecycle.MediatorLiveData

/**
 * [MediatorLiveData] subclass which provides a separate [LiveData] per each result returned by [keySelector] function
 * executed on subsequent values emitted by the source LiveData.
 */
class GroupedLiveData<K, V>(private val keySelector: (V) -> K): MediatorLiveData<V>() {

    private val results = mutableMapOf<K, MediatorLiveData<V>>()

    /**
     * Returns a [LiveData] emitting only values, for which the result of the [keySelector] function is equal
     * to the given [key].
     */
    operator fun get(key: K): LiveData<V> = getOrCreate(key)

    private fun getOrCreate(key: K) = synchronized(this) {
        results.getOrPut(key) { MediatorLiveData<V>() }.also { it.addSource(this) { } }
    }

    override fun setValue(value: V) {
        super.setValue(value)
        getOrCreate(keySelector(value)).value = value
    }
}
