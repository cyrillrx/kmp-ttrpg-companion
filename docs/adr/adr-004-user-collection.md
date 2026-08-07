# ADR-004: Rename the "User List" feature to "Collection"

**Status**: Accepted  
**Date**: 2026-08-05  
**Context**: Cleanup before implementation — the user-created-lists feature carries spell-specific, compendium-colliding wording that no longer matches its generic model.

## Decision

The user-created-lists feature is renamed to **Collection**.

- Domain model `UserList` → `UserCollection`. The `User` prefix is **kept** (not a bare `Collection`) to avoid clashing with `kotlin.collections.Collection`.
- Package `com.cyrillrx.rpg.userlist` → `com.cyrillrx.rpg.usercollection`; every feature symbol follows (`UserListsScreen` → `UserCollectionsScreen`, `AddToList*` → `AddToCollection*`, `ListDetail*` → `CollectionDetail*`, repositories, routes, router, factories, states).
- The `type` enum is unchanged: `UserCollection.Type { SPELL, MAGICAL_ITEM, MONSTER }` (values unchanged → no data migration for `type`).
- The SQLDelight table `UserList` is renamed to `UserCollection` through a data-preserving migration (schema v3 → v4).
- **UI wording**: the umbrella section becomes "My Collections"; the three typed entries become the content categories "Spells" / "Magical items" / "Bestiaries" (FR: "Mes collections"; "Sorts" / "Objets magiques" / "Bestiaires"). This removes "Spellbooks", which duplicated the spell-compendium label.

## Context

The feature stores user-created, named lists that can hold **spells, magical items, or monsters** (`Type { SPELL, MAGICAL_ITEM, MONSTER }`). It surfaced as "Spellbooks" / "My Lists", wording that is:

1. **Too narrow** — "Spell*" describes only one of three supported content types.
2. **Colliding with the compendium** — the read-only D&D 5e browsers are already called "Spellbook", "Bestiary", "Magical items". Naming the user lists the same way blurs "browse the SRD" and "manage my own lists".

The code identifiers were already neutral (`UserList`, package `userlist`), but "list" is a generic word and the displayed labels drifted toward compendium terms.

## Why "Collection" (generic), with the `User` prefix

A generic term matches the multi-type model and stays correct when item/monster lists get more UI. "Collection" reads well, is distinct from the compendium, and is future-proof.

The class is **`UserCollection`**, not `Collection`: `kotlin.collections.Collection` is in scope everywhere (the project even has `core/domain/CollectionExt.kt` extending it), so a bare `Collection` domain type would create constant import ambiguity.

## Why rename the SQLDelight table too

Full alignment down to the schema, so nothing internally still says "UserList". SQLite supports `ALTER TABLE … RENAME TO` (already used in `2.sqm`), so the rename is a one-line, data-preserving migration — no column changes, no transit table. It adds schema version 4, a new snapshot `databases/4.db`, and a migration-test case.

## Consequences

- New migration `sqldelight/…/cache/3.sqm`: `ALTER TABLE UserList RENAME TO UserCollection;`. `AppDatabase.sq` table + queries renamed (`saveUserList` → `saveUserCollection`, `selectAllUserListsByType` → `selectAllUserCollectionsByType`, …). Regenerated schema snapshot `databases/4.db`. `AppDatabaseMigrationTest` covers v3 → v4 with data preserved.
- The generated cache type `cache.UserList` → `cache.UserCollection`; the mapping in `core/data/cache/Database.kt` and `SQLDelightUserCollectionRepository` follows.
- String-resource keys carrying "list"/"spell_lists" are renamed to their "collection" equivalents in both `values/` and `values-fr/`.
- Maestro flows, test fixtures, and docs (`roadmap.md`, `docs/testing/e2e-test-cases.md`, PRD-001a Phase 2) are harmonized to "collection".
- The spell **compendium** ("Spellbook" singular, `btn_spell_book`) and all SRD/compendium data are **unchanged**.

**Saved navigation state**: the `@Serializable` `UserListRoute` NavKeys use their default (fully-qualified) serial names in the polymorphic back-stack serializer. Renaming the package/class changes those names, so a back stack persisted by an older build will not deserialize after update. We accept that reset rather than pinning legacy `@SerialName` values, to avoid freezing old package names into new code; the collection **data** is preserved by the DB migration either way.

The reset has to be implemented, though: an unknown discriminator makes polymorphic decoding throw, and `rememberNavBackStack` has no fallback, so restoring such a back stack would crash at launch. `navSerializersModule` therefore declares `defaultDeserializer { MainRoute.Home.serializer() }` — every unrecognized entry decodes as Home. A stack of N entries restores as N Home entries: degraded, but navigable, and it covers any future route move rather than just this rename.

## Alternatives considered

**Bare `Collection` model** — Rejected: clashes with `kotlin.collections.Collection`, forcing qualified imports or aliases throughout.

**Keep the table name `UserList`, rename Kotlin only** — Rejected: leaves the persistence layer misaligned; the SQLite table rename is cheap and data-preserving.

**Keep "Spell list" as the term** — Rejected: spell-specific, and the model is multi-type (spells / items / monsters).

**Per-type labels "Spell collections / Item collections / Bestiary collections"** — Rejected as verbose; the "My Collections" umbrella already disambiguates the content categories "Spells / Magical items / Bestiaries" from the compendium.
