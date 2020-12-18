# Changelog

## [Unreleased]
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
