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
class ReduceTest {

    @RelaxedMockK
    lateinit var stringObserver: Observer<String?>

    @RelaxedMockK
    lateinit var intObserver: Observer<Int>

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with reduce, " +
            "WHEN posting values to source, " +
            "THEN reduced value should be observed after each emitted item"
    )
    fun reduce() {
        val source = MutableLiveData<String>()
        val reduced = source.reduce { a, b -> "$a, $b" }
        reduced.observeForever(stringObserver)

        source.postValue("first")
        source.postValue("second")
        source.postValue(null)
        source.postValue("third")

        verifySequence {
            stringObserver.onChanged("first")
            stringObserver.onChanged("first, second")
            stringObserver.onChanged("first, second, null")
            stringObserver.onChanged("first, second, null, third")
        }
    }

    @Test
    @DisplayName(
        value = "GIVEN source LiveData with reduceNotNull, " +
            "WHEN posting values to source, " +
            "THEN reduced value should be observed after each emitted non-null item"
    )
    fun reduceNotNull() {
        val source = MutableLiveData<Int>()
        val reduced = source.reduceNotNull { a, b -> a + b }
        reduced.observeForever(intObserver)

        source.postValue(1)
        source.postValue(2)
        source.postValue(null)
        source.postValue(3)
        source.postValue(4)

        verifySequence {
            intObserver.onChanged(1)
            intObserver.onChanged(3)
            intObserver.onChanged(6)
            intObserver.onChanged(10)
        }
    }
}
