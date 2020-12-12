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

### Transformations

<details>
  <summary><code>mapNotNull</code></summary>

  Returns a [LiveData] emitting only the non-null results of applying the given `transform` function to each value
  emitted by this LiveData.

  ```kotlin
  val userOptionLiveData: LiveData<Option<User>> = // ...
  val userLiveData: LiveData<User> = userOptionLiveData.mapNotNull { user -> user.getOrNull() }
  ```
</details>

<details>
  <summary><code>filter</code></summary>

  Returns a [LiveData] emitting only values from this LiveData matching the given `predicate`.

  ```kotlin
  val resultLiveData: LiveData<Try<User>> = // ...
  val successLiveData: LiveData<Try<User>> = resultLiveData.filter { it.isSuccess }
  ```
</details>

<details>
  <summary><code>filterNotNull</code></summary>

  Returns a [LiveData] emitting only non-null values from this LiveData.

  ```kotlin
  val userLiveData: LiveData<User?> = // ...
  val nonNullUserLiveData: LiveData<User> = userLiveData.filterNotNull()
  ```
</details>

<details>
  <summary><code>filterIsInstance</code></summary>

  Returns a [LiveData] emitting only values of the given type from this LiveData.

  ```kotlin
  val resultLiveData: LiveData<Try<User>> = // ...
  val failureLiveData: LiveData<Failure> = resultLiveData.filterIsInstance<Failure>()
  ```
</details>

<details>
  <summary><code>reduce</code></summary>

  Returns a [LiveData] emitting accumulated value starting with the first value emitted by this LiveData and applying
  `operation` from left to right to current accumulator value and each value emitted by this.

  ```kotlin
  val newOperationsCountLiveData: LiveData<Int?> = // ...
  val operationsCountLiveData: LiveData<Int?> =
      newOperationsCountLiveData.reduce { acc, next -> if (next == null) null else acc + next }
  ```
</details>

<details>
  <summary><code>reduceNotNull</code></summary>

  Returns a [LiveData] emitting non-null accumulated value starting with the first non-null value emitted by this
  LiveData and applying `operation` from left to right to current accumulator value and each subsequent non-null value
  emitted by this LiveData.

  ```kotlin
  val newOperationsCountLiveData: LiveData<Int> = // ...
  val operationsCountLiveData: LiveData<Int> =
      newOperationsCountLiveData.reduceNotNull { acc, next -> acc + next }
  ```
</details>

<details>
  <summary><code>throttleWithTimeout</code></summary>

  Returns a [LiveData] emitting values from this LiveData, after dropping values followed by newer values before
  `timeInMillis` expires.

  ```kotlin
  val isLoadingLiveData: LiveData<Boolean> = // ...
  val isLoadingThrottledLiveData: LiveData<Boolean> = isLoadingLiveData.throttleWithTimeout(
      timeInMillis = 1000L,
      context = viewModelScope.coroutineContext
  )
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


[LiveData]: https://developer.android.com/reference/androidx/lifecycle/LiveData
[InstantTaskExecutorRule]: https://developer.android.com/reference/androidx/arch/core/executor/testing/InstantTaskExecutorRule
[TestCoroutineDispatcher]: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-coroutine-dispatcher/
