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
 * Returns a [LiveData] emitting pairs of latest values emitted by the [first] and the [second] LiveData.
 *
 * **Example:**
 * ```
 * val userLiveData: LiveData<User> = ...
 * val avatarUrlLiveData: LiveData<String> = ...
 * val userWithAvatar: LiveData<Pair<User?, String?>> = combineLatest(userLiveData, avatarUrlLiveData)
 * ```
 */
fun <A, B> combineLatest(first: LiveData<A>, second: LiveData<B>): LiveData<Pair<A?, B?>> {
    val result = MediatorLiveData<Pair<A?, B?>>()
    result.addSource(first) { a -> result.value = a to result.value?.second }
    result.addSource(second) { b -> result.value = result.value?.first to b }
    return result
}

/**
 * Returns a [LiveData] emitting triples of latest values emitted by the [first], the [second] and the [third] LiveData.
 */
fun <A, B, C> combineLatest(first: LiveData<A>, second: LiveData<B>, third: LiveData<C>): LiveData<Triple<A?, B?, C?>> {
    val result = MediatorLiveData<Triple<A?, B?, C?>>()
    result.addSource(first) { a -> result.value = Triple(a, result.value?.second, result.value?.third) }
    result.addSource(second) { b -> result.value = Triple(result.value?.first, b, result.value?.third) }
    result.addSource(third) { c -> result.value = Triple(result.value?.first, result.value?.second, c) }
    return result
}

/**
 * Returns a [LiveData] emitting lists of latest values emitted by the given source LiveData.
 */
fun <T> combineLatest(vararg sources: LiveData<T>): LiveData<List<T?>> {
    val result = MediatorLiveData<List<T?>>()
    for ((index, source) in sources.withIndex()) {
        result.addSource(source) { x ->
            val itemsBefore = result.value?.take(index) ?: List(size = index) { null }
            val itemsAfter = result.value?.drop(index + 1) ?: List(size = sources.size - index - 1) { null }
            result.value = itemsBefore + x + itemsAfter
        }
    }
    return result
}

/**
 * Returns a [LiveData] emitting results of applying the given [combineFunction] to the latest values emitted by the
 * [first] and the [second] LiveData.
 *
 * [combineFunction] will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val userLiveData: LiveData<User> = ...
 * val avatarUrlLiveData: LiveData<String> = ...
 * val userWithAvatar: LiveData<UserWithAvatar> =
 *     combineLatest(userLiveData, avatarUrlLiveData) { user, avatarUrl ->
 *         UserWithAvatar(user, avatarUrl)
 *     }
 * ```
 */
fun <A, B, R> combineLatest(first: LiveData<A>, second: LiveData<B>, combineFunction: (A?, B?) -> R): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(first) { a -> result.value = combineFunction(a, second.value) }
    result.addSource(second) { b -> result.value = combineFunction(first.value, b) }
    return result
}

/**
 * Returns a [LiveData] emitting results of applying the given [combineFunction] to the latest values emitted by the
 * [first], the [second] and the [third] LiveData.
 *
 * [combineFunction] will be executed on the main thread.
 */
fun <A, B, C, R> combineLatest(
    first: LiveData<A>,
    second: LiveData<B>,
    third: LiveData<C>,
    combineFunction: (A?, B?, C?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(first) { a -> result.value = combineFunction(a, second.value, third.value) }
    result.addSource(second) { b -> result.value = combineFunction(first.value, b, third.value) }
    result.addSource(third) { c -> result.value = combineFunction(first.value, second.value, c) }
    return result
}

/**
 * Returns a [LiveData] emitting results of applying the given [combineFunction] to the latest values emitted by the
 * given source LiveData.
 *
 * [combineFunction] will be executed on the main thread.
 */
fun <T, R> combineLatest(vararg sources: LiveData<T>, combineFunction: (List<T?>) -> R): LiveData<R> {
    val result = MediatorLiveData<R>()
    for ((index, source) in sources.withIndex()) {
        result.addSource(source) { x ->
            val itemsBefore = sources.take(index).map { it.value }
            val itemsAfter = sources.drop(index + 1).map { it.value }
            result.value = combineFunction(itemsBefore + x + itemsAfter)
        }
    }
    return result
}
