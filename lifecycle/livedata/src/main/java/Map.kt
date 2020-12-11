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
