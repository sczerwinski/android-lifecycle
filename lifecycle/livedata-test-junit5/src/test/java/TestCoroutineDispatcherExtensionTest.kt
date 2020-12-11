package it.czerwinski.android.lifecycle.test.junit5

import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class, TestCoroutineDispatcherExtension::class)
@DisplayName("Tests for TestCoroutineDispatcherExtension")
class TestCoroutineDispatcherExtensionTest {

    @RelaxedMockK
    lateinit var intObserver: Observer<Int>

    @Test
    @DisplayName(
        value = "GIVEN mocked observer for CoroutineLiveData, " +
            "WHEN verifySequence, " +
            "THEN emitted values should be observed"
    )
    fun testWithoutTestCoroutineDispatcherExtension() {
        val liveData = liveData {
            emit(1)
        }

        liveData.observeForever(intObserver)

        verifySequence {
            intObserver.onChanged(1)
        }
    }
}
