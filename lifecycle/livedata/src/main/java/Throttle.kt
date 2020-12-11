package it.czerwinski.android.lifecycle

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
