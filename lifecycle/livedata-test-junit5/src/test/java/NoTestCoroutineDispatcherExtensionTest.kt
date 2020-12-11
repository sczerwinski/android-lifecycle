package it.czerwinski.android.lifecycle.test.junit5

import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
@DisplayName("Tests without TestCoroutineDispatcherExtension")
class NoTestCoroutineDispatcherExtensionTest {

    @RelaxedMockK
    lateinit var intObserver: Observer<Int>

    @Test
    @DisplayName(
        value = "GIVEN mocked observer for CoroutineLiveData, " +
            "WHEN verifySequence, " +
            "THEN should throw exception"
    )
    fun testWithoutTestCoroutineDispatcherExtension() {
        val liveData = liveData {
            emit(1)
        }

        liveData.observeForever(intObserver)

        assertThrows<Exception> {
            verifySequence {
                intObserver.onChanged(1)
            }
        }
    }
}
