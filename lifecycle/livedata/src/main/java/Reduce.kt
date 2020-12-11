package it.czerwinski.android.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * Returns a [LiveData] emitting non-null accumulated value starting with the first non-null value
 * emitted by this LiveData and applying [operation] from left to right to current accumulator value
 * and each subsequent non-null value emitted by this LiveData.
 *
 * [operation] will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val newOperationsCountLiveData: LiveData<Int> = ...
 * val operationsCountLiveData: LiveData<Int> =
 *     newOperationsCountLiveData.reduceNotNull { acc, next -> acc + next }
 * ```
 */
fun <T> LiveData<T>.reduceNotNull(
    operation: (T, T) -> T
): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { x ->
        if (x != null) {
            val oldValue = result.value
            result.value =
                if (oldValue == null) x
                else operation(oldValue, x)
        }
    }
    return result
}

/**
 * Returns a [LiveData] emitting accumulated value starting with the first value emitted by this
 * LiveData and applying [operation] from left to right to current accumulator value and each value
 * emitted by this.
 *
 * [operation] will be executed on the main thread.
 *
 * **Example:**
 * ```
 * val newOperationsCountLiveData: LiveData<Int?> = ...
 * val operationsCountLiveData: LiveData<Int?> =
 *     newOperationsCountLiveData.reduce { acc, next -> if (next == null) null else acc + next }
 * ```
 */
fun <T> LiveData<T?>.reduce(
    operation: (T?, T?) -> T?
): LiveData<T?> {
    val result = MediatorLiveData<T>()
    result.addSource(this, object : Observer<T?> {

        private var firstTime = true

        override fun onChanged(x: T?) {
            if (firstTime) {
                firstTime = false
                result.value = x
            } else {
                result.value = operation(result.value, x)
            }
        }
    })
    return result
}
