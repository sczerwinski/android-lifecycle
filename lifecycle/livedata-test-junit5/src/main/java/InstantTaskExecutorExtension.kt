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

package it.czerwinski.android.lifecycle.test.junit5

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.runner.Description

/**
 * JUnit5 extension that swaps the background executor used by the Architecture Components with a different one which
 * executes each task synchronously.
 *
 * This extension is analogous to [InstantTaskExecutorRule] for JUnit4.
 */
class InstantTaskExecutorExtension : InstantTaskExecutorRule(), BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        starting(Description.createTestDescription(context?.requiredTestClass, context?.displayName))
    }

    override fun afterEach(context: ExtensionContext?) {
        finished(Description.createTestDescription(context?.requiredTestClass, context?.displayName))
    }
}
