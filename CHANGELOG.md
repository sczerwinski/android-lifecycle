# Changelog

## [Unreleased]
### Added
- JUnit4 rules for testing LiveData:
  - `TestCoroutineDispatcherRule`

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
