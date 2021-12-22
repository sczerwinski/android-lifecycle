/*
 * Copyright 2020 Slawomir Czerwinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package it.czerwinski.android.lifecycle.livedata.test.junit5

import androidx.lifecycle.liveData
import it.czerwinski.android.lifecycle.livedata.test.test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("Tests without TestCoroutineDispatcherExtension")
class NoTestCoroutineDispatcherExtensionTest {

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

        assertThrows<Exception> {
            liveData.test().values()
        }
    }
}
