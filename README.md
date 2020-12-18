[![Build](https://github.com/sczerwinski/android-lifecycle/workflows/Build/badge.svg)][ci-build]

# Extensions for Jetpack Lifecycle

## LiveData Extensions

[![Maven Central](https://img.shields.io/maven-central/v/it.czerwinski.android.lifecycle/lifecycle-livedata)][lifecycle-livedata-release]
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/it.czerwinski.android.lifecycle/lifecycle-livedata?server=https%3A%2F%2Foss.sonatype.org)][lifecycle-livedata-snapshot]

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

### Types

<details>
  <summary><code>ConstantLiveData</code></summary>

  [LiveData] that always emits a single constant value.
</details>

<details>
  <summary><code>GroupedLiveData</code></summary>

  [MediatorLiveData] subclass which provides a separate [LiveData] per each result returned by `keySelector` function
  executed on subsequent values emitted by the source LiveData.
</details>

### LiveData Transformations

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

<details>
  <summary><code>delayStart</code></summary>

  Returns a [LiveData] emitting values from this LiveData, after dropping values followed by newer values before
  `timeInMillis` expires since the result LiveData has been created.

  ```kotlin
  val resultLiveData: LiveData<ResultData> = // ...
  val delayedResultLiveData: LiveData<ResultData> = resultLiveData.delayStart(
      timeInMillis = 1000L,
      context = viewModelScope.coroutineContext
  )
  ```
</details>

<details>
  <summary><code>merge</code></summary>

  Returns a [LiveData] emitting each value emitted by any of the given LiveData.

  ```kotlin
  val serverError: LiveData<String> = // ...
  val databaseError: LiveData<String> = // ...
  val error: LiveData<String> = serverError merge databaseError
  ```

  ```kotlin
  val serverError: LiveData<String> = // ...
  val databaseError: LiveData<String> = // ...
  val fileError: LiveData<String> = // ...
  val error: LiveData<String> = merge(serverError, databaseError, fileError)
  ```
</details>

<details>
  <summary><code>combineLatest</code></summary>

  Returns a [LiveData] emitting pairs, triples or lists of latest values emitted by the given LiveData.

  ```kotlin
  val userLiveData: LiveData<User> = // ...
  val avatarUrlLiveData: LiveData<String> = // ...
  val userWithAvatar: LiveData<Pair<User?, String?>> = combineLatest(userLiveData, avatarUrlLiveData)
  ```

  ```kotlin
  val userLiveData: LiveData<User> = // ...
  val avatarUrlLiveData: LiveData<String> = // ...
  val userWithAvatar: LiveData<UserWithAvatar> =
      combineLatest(userLiveData, avatarUrlLiveData) { user, avatarUrl ->
          UserWithAvatar(user, avatarUrl)
      }
  ```
</details>

<details>
  <summary><code>switch</code></summary>

  Converts [LiveData] that emits other LiveData into a single LiveData that emits the items emitted by the most
  recently emitted LiveData.

  ```kotlin
  val sourcesLiveData: LiveData<LiveData<String>> = // ...
  val resultLiveData: LiveData<String?> = sourcesLiveData.switch()
  ```
</details>

<details>
  <summary><code>groupBy</code></summary>

  Returns a `GroupedLiveData` providing a set of [LiveData], each emitting a different subset of values from this
  LiveData, based on the result of the given `keySelector` function.

  ```kotlin
  val userLiveData: LiveData<User> = // ...
  val userByStatusLiveData: GroupedLiveData<UserStatus, User> = errorLiveData.groupBy { user -> user.status }
  val activeUserLiveData: LiveData<User> = userByStatusLiveData[UserStatus.ACTIVE]
  ```
</details>

<details>
  <summary><code>defaultIfEmpty</code></summary>

  Returns a [LiveData] that emits the values emitted by this LiveData or a specified default value if this LiveData has
  not yet emitted any values at the time of observing.

  ```kotlin
  val errorLiveData: LiveData<String> = // ...
  val statusLiveData: LiveData<String?> = errorLiveData.defaultIfEmpty("No errors")
  ```
</details>

### MediatorLiveData Extensions

<details>
  <summary><code>addDirectSource</code></summary>

  Starts to listen the given [source] LiveData.
  Whenever [source] value is changed, it is set as a new value of this [MediatorLiveData].

  ```kotlin
  mediatorLiveData.addDirectSource(liveData)
  ```
  is equivalent to:
  ```kotlin
  mediatorLiveData.addSource(liveData) { x -> mediatorLiveData.value = x }
  ```
</details>

## LivaData Testing Utilities

[![Maven Central](https://img.shields.io/maven-central/v/it.czerwinski.android.lifecycle/lifecycle-livedata-test-junit5)][lifecycle-livedata-test-junit5-release]
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/it.czerwinski.android.lifecycle/lifecycle-livedata-test-junit5?server=https%3A%2F%2Foss.sonatype.org)][lifecycle-livedata-test-junit5-snapshot]

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


[ci-build]: https://github.com/sczerwinski/android-lifecycle/actions?query=workflow%3ABuild
[lifecycle-livedata-release]: https://repo1.maven.org/maven2/it/czerwinski/android/lifecycle/lifecycle-livedata/
[lifecycle-livedata-test-junit5-release]: https://repo1.maven.org/maven2/it/czerwinski/android/lifecycle/lifecycle-livedata-test-junit5/
[lifecycle-livedata-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/it/czerwinski/android/lifecycle/lifecycle-livedata/
[lifecycle-livedata-test-junit5-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/it/czerwinski/android/lifecycle/lifecycle-livedata-test-junit5/

[LiveData]: https://developer.android.com/reference/androidx/lifecycle/LiveData
[MediatorLiveData]: https://developer.android.com/reference/androidx/lifecycle/MediatorLiveData
[InstantTaskExecutorRule]: https://developer.android.com/reference/androidx/arch/core/executor/testing/InstantTaskExecutorRule
[TestCoroutineDispatcher]: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-coroutine-dispatcher/
