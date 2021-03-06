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

package it.czerwinski.android.lifecycle.livedata.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Creates a non-forwarding [TestObserver] observing all values emitted by this [LiveData].
 *
 * @since 1.1.0
 */
fun <T> LiveData<T>.test(): TestObserver<T> {
    val observer = TestObserver.create<T>()
    observeForever(observer)
    return observer
}

/**
 * Creates a forwarding [TestObserver] observing all values emitted by this [LiveData].
 *
 * @since 1.1.0
 */
fun <T> LiveData<T>.test(downstream: Observer<T>): TestObserver<T> {
    val observer = TestObserver.create(downstream)
    observeForever(observer)
    return observer
}
