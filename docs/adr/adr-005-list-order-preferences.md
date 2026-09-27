# ADR-005: Store each list's chosen order in the preferences row

**Status**: Accepted  
**Date**: 2026-09-27  
**Context**: Three screens gained a choice of order (#256, #267, #269), but the choice was lost at every launch.

## Decision

Each list keeps its own order, stored as a column on the single-row `UserPreferencesEntity` table.

- Three columns: `character_sort_order` and `collection_sort_order` (both `StoredSortOrder`, defaulting to `last_modified`), and `collection_item_order` (`CollectionItemOrder`, defaulting to `added`).
- Values are stored as the **lower-cased enum name**, read back with a case-insensitive lookup that falls back to the default — the convention `theme`, `palette` and `distance_unit` already follow.
- Schema v4 → v5 through `4.sqm`, three `ALTER TABLE … ADD COLUMN` with those defaults, and a regenerated snapshot `databases/5.db`.
- `CollectionItemOrder` moves from `composeApp/…/usercollection/presentation/` to `shared/core/…/usercollection/domain/`, so that `UserPreferences` can name it.

## Context

The lists are ordered in the presentation layer: `applySort` for the timestamped lists and `applyOrder` for a collection's entries, each driven by a criterion held in the screen's state. That state dies with the screen, so a user who prefers their sheets by name re-selected it on every launch.

`UserPreferencesEntity` already stores the three settings the app persists, with a repository exposing a `StateFlow` and a settings screen writing to it. Nothing else in the app persists UI state.

## Why one preference per screen

Sorting the character sheets by name says nothing about how the collections should be ordered: these are different lists, read at different moments, for different reasons. A single shared criterion would also be ill-typed — the two list screens use `StoredSortOrder`, a collection's contents use `CollectionItemOrder`, which has no `LAST_MODIFIED` because its entries carry no date.

## Why columns rather than a key/value table

A `ListPreference(key, value)` table would absorb new screens without a migration, which is its only advantage. Against it: three values known at compile time lose their type, the read becomes a lookup that can miss, and the app gains a second preference mechanism next to the one it already has. Three columns cost one migration today and stay consistent with `theme`, `palette` and `distance_unit`.

## Why the enum moves to the shared module

`UserPreferences` lives in `shared/core`, which cannot depend on `composeApp`. `CollectionItemOrder` describes how a collection's entries are ordered — a property of the entity, not of the screen that displays it — so `usercollection/domain`, next to `UserCollection`, is where it belongs. `applySort`'s counterpart `applyOrder` follows it, using only `Entity` and `sortedByName`, both already in `shared/core`.

## Consequences

- New migration `sqldelight/…/cache/4.sqm` and snapshot `databases/5.db`; `AppDatabaseMigrationTest` gains a v4 witness and a v4 → v5 case, and its v1 case now asserts every column the migrations have added since.
- `Database.getUserPreferences` projects seven columns instead of four; the lambda arity change breaks compilation at every call site, which is the intended safety net.
- `UserPreferencesRepository` gains three setters, each short-circuiting when the value is unchanged, as the existing ones do.
- The list view models receive `UserPreferencesRepository`: they read their initial criterion from `preferences.value` — safe, since `App.kt` blocks composition until `initialize()` returns — and write it back on change. `handleCharacterRoutes`, `handleUserCollectionRoutes` and the three compendium route builders that construct a collection detail must therefore be handed the repository.
- A stored value that no longer maps to an enum constant — an order removed in a later version — reads back as the default rather than crashing.

## Alternatives considered

**A key/value preferences table** — Rejected: untyped and lossy for three values known in advance, and a second preference mechanism alongside the existing one.

**One shared criterion for every list** — Rejected: couples unrelated screens, and cannot type the collection contents, whose criteria differ.

**An order per collection, stored on `UserCollection`** — Rejected for now: a column on the entity for a need nothing supports yet. The shared preference can be narrowed later without a data change.

**Keeping the choice in memory** — the status quo: rejected, it is precisely what #256 promised to fix.
