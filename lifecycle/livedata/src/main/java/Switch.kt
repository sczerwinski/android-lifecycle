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
import androidx.lifecycle.Observer

/**
 * Converts [LiveData] that emits other LiveData into a single LiveData that emits the items emitted by the most
 * recently emitted LiveData.
 */
fun <T> LiveData<LiveData<T>>.switch(): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this, object : Observer<LiveData<T>?> {

        var source: LiveData<T>? = null

        override fun onChanged(t: LiveData<T>?) {
            val oldSource = source
            val newSource: LiveData<T>? = t
            if (oldSource === newSource) {
                return
            }
            if (oldSource != null) {
                result.removeSource(oldSource)
            }
            source = newSource
            if (newSource != null) {
                result.addDirectSource(newSource)
            }
        }
    })
    return result
}
