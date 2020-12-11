package it.czerwinski.android.lifecycle.test.junit5

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@DisplayName("Tests without InstantTaskExecutorExtension")
class NoInstantTaskExecutorExtensionTest {

    @RelaxedMockK
    lateinit var intObserver: Observer<Int>

    @Test
    @DisplayName(
        value = "GIVEN LiveData, " +
            "WHEN observeForever, " +
            "THEN should throw RuntimeException"
    )
    fun testWithoutInstantTaskExecutorExtension() {
        val liveData = MutableLiveData<Int>()

        assertThrows<RuntimeException> {
            liveData.observeForever(intObserver)
        }
    }
}
