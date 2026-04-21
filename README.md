# AMRO — Advanced Movie Recommendation Organisation

An Android MVP showcasing a scalable, modular architecture for browsing trending
movies via TMDB. Built as a take-home assessment.

![CI](https://github.com/diederv/Amro/actions/workflows/ci.yml/badge.svg)

## Quick start

1. Get a TMDB API key: https://www.themoviedb.org/settings/api
2. Add `TMDB_API_KEY=your_key_here` to `local.properties` in the project root
3. Run `./gradlew :app:assembleDebug` or open in Android Studio

## What's in the app

- **Trending Movies** — top 100 trending this week, filter by genre, sort by popularity/title/release date (asc/desc)
- **Movie Details** — full info including budget, revenue, vote count, IMDb link
- Offline-first: works without network after first fetch
- Light + dark themes with Material 3 dynamic color on Android 12+
- Shared element transition between list poster and detail hero image
- Accessibility: TalkBack-friendly, 48dp touch targets, semantic headings, merged list item semantics

## Architecture

Multi-module, offline-first, Clean-lite:

```
:app                    Composition root, navigation, DI wiring
:core:designsystem      AmroTheme (M3 + dynamic color), reusable composables
:core:network           Retrofit + TMDB API service + AuthInterceptor
:core:database          Room database, DAOs, TypeConverters
:core:model             Pure Kotlin domain models + sealed Result<T>
:feature:movies         List + detail screens, ViewModels, use cases, repository
```

Data flow: `TMDB API → Repository → Room (single source of truth) → Flow → ViewModel → UiState → Composable`

### Key choices and why

- **Koin for DI** — simpler setup for this MVP size, DSL reads well, no annotation processor. Explicitly mentioned in the target job req.
- **Offline-first with Room** — Room is the single source of truth; the repository refreshes from TMDB and writes to Room, UI only observes Room. Directly addresses AMRO's future offline ambition.
- **Clean-lite architecture** — data/domain/UI separation with use cases where they earn their keep (e.g. `FilterAndSortMoviesUseCase`), not every action wrapped for its own sake.
- **Sealed Result + UiState** — errors as values in the data/domain layer, UI maps to a sealed `UiState` with explicit Loading/Content/Empty/Error variants.
- **Kotlinx.serialization** — modern, Kotlin-native, no annotation processor, builds faster than Moshi+KAPT.
- **6-module shape** — addresses AMRO's "multiple feature teams" ambition without over-engineering a 2-screen MVP. A future `:feature:actors` team can reuse `:core:designsystem` and the repository pattern without touching movies code.

## Testing strategy

A pyramid approach:

- **Unit tests (majority)** — ViewModels, repository, use cases, mappers. JUnit4 + Turbine for StateFlow + `runTest` with `StandardTestDispatcher`.
- **Compose UI tests (a handful)** — key screen behaviours: filter selection updates visible items, detail renders all fields, error state shows retry. `createComposeRule()` for speed.
- **Screenshot tests (focused)** — Paparazzi snapshots of list item and screens in light + dark. Catches visual regressions at PR time.

I use **fakes** (hand-written test doubles) for repositories and data sources — `FakeMovieRepository`, `FakeMovieDao`, `FakeGenreDao`, `FakeTmdbApiService`. Fakes are more honest for behaviour-heavy classes; mocks are better for narrow call-verification.

Run: `./gradlew test` (unit + screenshot) or `./gradlew connectedAndroidTest` (Compose instrumented).

## Scaling for the future

- **More feature teams**: add `:feature:actors`, `:feature:profile`, etc. Each is a module that talks to `:core:*`. No feature-to-feature dependencies.
- **More API sources**: repository already returns domain types via a mapper. A second data source becomes a second `*DataSource` behind the same repository interface.
- **Offline**: Room is already the single source of truth. Full offline requires one more step — a `WorkManager` periodic sync job — but the foundation is there.

## Trade-offs I'd revisit with more time

- **No pagination** — top 100 is small enough that a single fetch is fine, but I'd add Paging 3 if this grew to "top 1000".
- **No search** — out of scope for MVP, but the repository is shaped to support it.
- **Shared element transition** — used the new Compose API (`SharedTransitionLayout`); works well but has some corner cases around navigation back-stack. In production I'd add a UI test specifically for rotation during the transition.

## Tech stack

Kotlin 2.0, Jetpack Compose, Material 3, Coroutines + Flow, Koin 4, Retrofit 2, Kotlinx.serialization, Room, Coil, Navigation Compose 2.8 (type-safe routes), Turbine, Paparazzi, GitHub Actions.
