package it.czerwinski.android.lifecycle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import it.czerwinski.android.lifecycle.test.junit5.InstantTaskExecutorExtension
import it.czerwinski.android.lifecycle.test.junit5.TestCoroutineDispatcherExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class, TestCoroutineDispatcherExtension::class)
class ThrottleTest {

    @RelaxedMockK
    lateinit var intObserver: Observer<Int>

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with throttleWithTimeout, " +
            "WHEN posting multiple values to source, " +
            "THEN only the latest values after timeout should be observed"
    )
    fun throttleWithTimeout() {
        val dispatcher = TestCoroutineDispatcher()

        val source = MutableLiveData<Int>()
        val throttled = source.throttleWithTimeout(timeInMillis = 9_000L, context = dispatcher)
        throttled.observeForever(intObserver)

        source.postValue(1)
        dispatcher.advanceTimeBy(delayTimeMillis = 8_000L)
        source.postValue(2)
        dispatcher.advanceTimeBy(delayTimeMillis = 8_000L)
        source.postValue(3)
        dispatcher.advanceTimeBy(delayTimeMillis = 8_000L)
        source.postValue(4)
        dispatcher.advanceTimeBy(delayTimeMillis = 10_000L)
        source.postValue(5)
        dispatcher.advanceTimeBy(delayTimeMillis = 10_000L)
        source.postValue(6)
        dispatcher.advanceTimeBy(delayTimeMillis = 8_000L)
        source.postValue(7)
        dispatcher.advanceTimeBy(delayTimeMillis = 10_000L)

        verifySequence {
            intObserver.onChanged(4)
            intObserver.onChanged(5)
            intObserver.onChanged(7)
        }
    }
}
