package it.czerwinski.android.lifecycle.test.junit5

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
@DisplayName("Tests for InstantTaskExecutorExtension")
class InstantTaskExecutorExtensionTest {

    @RelaxedMockK
    lateinit var intObserver: Observer<Int>

    @Test
    @DisplayName(
        value = "GIVEN LiveData, " +
            "WHEN posting values to source, " +
            "THEN values should be observed"
    )
    fun testWithInstantTaskExecutorExtension() {
        val liveData = MutableLiveData<Int>()
        liveData.observeForever(intObserver)

        liveData.postValue(1)
        liveData.postValue(2)
        liveData.postValue(3)

        verifySequence {
            intObserver.onChanged(1)
            intObserver.onChanged(2)
            intObserver.onChanged(3)
        }
    }
}
