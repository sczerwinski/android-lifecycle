package it.czerwinski.android.lifecycle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import it.czerwinski.android.lifecycle.test.junit5.InstantTaskExecutorExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
class MapTest {

    @RelaxedMockK
    lateinit var stringObserver: Observer<String?>

    @Test
    @DisplayName(
        value = "GIVEN source LiveData transformed with mapNotNull, " +
            "WHEN posting values to source, " +
            "THEN only non-null transformed values should be observed"
    )
    fun mapNotNull() {
        val source = MutableLiveData<Int>()
        val transformed = source.mapNotNull { number ->
            if (number % 2 == 0) number.toString()
            else null
        }
        transformed.observeForever(stringObserver)

        source.postValue(1)
        source.postValue(2)
        source.postValue(3)
        source.postValue(4)

        verifySequence {
            stringObserver.onChanged("2")
            stringObserver.onChanged("4")
        }
    }
}
