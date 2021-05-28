# Changelog

## [Unreleased]
### Added
- `intervalLiveData` factory method

### Changed
- Upgrade Gradle to `6.9`
- Dependencies:
  - Upgrade Kotlin to `1.5.10`
  - Upgrade Android Gradle Plugin to `4.2.1`
  - Upgrade `io.gitlab.arturbosch.detekt` to `1.17.1`
  - Upgrade `org.jetbrains.kotlinx:kotlinx-coroutines-test` to `1.5.0-native-mt`
  - Upgrade `lifecycle-livedata-ktx` to `2.3.1`
  - Upgrade JUnit to `5.7.2`
  - Upgrade Dokka to `1.4.32`

## [1.1.0]
### Changed
- Upgrade Gradle to `6.8.2`
- Dependencies:
  - Upgrade `android-junit5` to `1.7.1.1`
  - Upgrade `lifecycle-livedata-ktx` to `2.3.0`

## [1.1.0-RC1]
### Changed
- Upgrade Gradle to `6.8`
- Dependencies:
  - Upgrade Kotlin to `1.4.30`
  - Upgrade Android Gradle Plugin to `4.1.2`

## [1.1.0-BETA1]
No changes since 1.1.0-ALPHA1

## [1.1.0-ALPHA1]
### Added
- `TestObserver` with assertions for observed values emitted by LiveData

### Changed
- Dependencies:
  - Upgrade `org.jetbrains.changelog` to `1.0.1`

### Removed
- Remove `io.mockk:mockk` test dependency

## [1.0.0]
No changes since 1.0.0-RC1

## [1.0.0-RC1]
### Added
- JUnit4 rules for testing LiveData:
  - `TestCoroutineDispatcherRule`

### Changed
- Dependencies:
  - Upgrade `io.gitlab.arturbosch.detekt` to `1.15.0`
  - Upgrade `org.jetbrains.kotlinx:kotlinx-coroutines-test` to `1.4.2-native-mt`
  - Upgrade `io.mockk:mockk` to `1.10.3-jdk8`

## [1.0.0-BETA3]
### Changed
- **BREAKING CHANGE:** Changed package names in `lifecycle-livedata` and `lifecycle-livedata-test-junit5`

## [1.0.0-BETA2]
### Added
- Extensions for MediatorLiveData:
  - `addDirectSource`

## [1.0.0-BETA1]
### Added
- `GroupedLiveData`
- Transformations for LiveData:
  - `delayStart`
  - `switch`
  - `groupBy`

## [1.0.0-ALPHA2]
### Added
- `ConstantLiveData`
- Transformations for LiveData:
  - `merge`
  - `combineLatest`
  - `defaultIfEmpty`

## [1.0.0-ALPHA1]
### Added
- Transformations for LiveData:
  - `mapNotNull`
  - `filter`
  - `filterNotNull`
  - `filterIsInstance`
  - `reduce`
  - `reduceNotNull`
  - `throttleWithTimeout`
- JUnit5 extensions for testing LiveData:
  - `InstantTaskExecutorExtension`
  - `TestCoroutineDispatcherExtension`
