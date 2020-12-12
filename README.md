![Build](https://github.com/sczerwinski/android-lifecycle/workflows/Build/badge.svg)

# Extensions for Jetpack Lifecycle

## LiveData Extensions

![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/it.czerwinski.android.lifecycle/lifecycle-livedata?server=https%3A%2F%2Foss.sonatype.org)

<details>
  <summary>Kotlin</summary>

  ```kotlin
  dependencies {
      implementation("it.czerwinski.android.lifecycle:lifecycle-livedata:[VERSION]")
  }
  ```
</details>

<details>
  <summary>Groovy</summary>

  ```groovy
  dependencies {
      implementation 'it.czerwinski.android.lifecycle:lifecycle-livedata:[VERSION]'
  }
  ```
</details>

## LivaData Testing Utilities

![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/it.czerwinski.android.lifecycle/lifecycle-livedata-test-junit5?server=https%3A%2F%2Foss.sonatype.org)

<details>
  <summary>Kotlin</summary>

  ```kotlin
  dependencies {
      testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
      testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
      testImplementation("it.czerwinski.android.lifecycle:lifecycle-livedata-test-junit5:[VERSION]")
  }
  ```
</details>

<details>
  <summary>Groovy</summary>

  ```groovy
  dependencies {
      testImplementation 'org.junit.jupiter:junit-jupiter-api:5.7.0'
      testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.7.0'
      testImplementation 'it.czerwinski.android.lifecycle:lifecycle-livedata-test-junit5:[VERSION]'
  }
  ```
</details>

### JUnit5 Extensions

<details>
  <summary><code>InstantTaskExecutorExtension</code></summary>

  JUnit5 extension that swaps the background executor used by the Architecture Components with a different one which
  executes each task synchronously.

  This extension is analogous to [InstantTaskExecutorRule] for JUnit4.

  ```kotlin
  @ExtendWith(InstantTaskExecutorExtension::class)
  class MyTestClass {
      // ...
  }
  ```
</details>

<details>
  <summary><code>TestCoroutineDispatcherExtension</code></summary>

  JUnit5 extension that swaps main coroutine dispatcher with [TestCoroutineDispatcher].

  ```kotlin
  @ExtendWith(TestCoroutineDispatcherExtension::class)
  class MyTestClass {
      // ...
  }
  ```
</details>


[InstantTaskExecutorRule]: https://developer.android.com/reference/androidx/arch/core/executor/testing/InstantTaskExecutorRule
[TestCoroutineDispatcher]: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-coroutine-dispatcher/
