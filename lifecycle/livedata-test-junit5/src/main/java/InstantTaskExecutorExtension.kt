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
