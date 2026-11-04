# ADR-007: Tag a recorded roll with the check it was rolled for

**Status**: Accepted  
**Date**: 2026-11-03  
**Context**: the character sheet ([PRD-002](../prd/prd-002-character-sheet.md)) will roll its skills, saving throws and initiative, and those rolls go to the history kept by [ADR-006](adr-006-dice-roll-history.md).

## Decision

A recorded roll may carry a context: the character it was rolled for and the check it was rolled as.

- Two nullable columns on `DiceRoll`: `characterId` and `checkKey`, with an index on `characterId` to filter the history by character.
- In the domain, `RollContext(characterId, check: D20Check)`: both are set or neither is. A roll from the roller has no context.
- `checkKey` is a stable, untranslated key: `save:WISDOM`, `skill:PERCEPTION`, `initiative`.
- The history stays a single global log, and the `MAX_RECORDED_ROLLS` cap stays global.
- Schema v6 → v7 through `6.sqm`, two `ALTER TABLE` and a `CREATE INDEX`, and a regenerated snapshot `databases/7.db`.

## Context

The roller records a bare roll. A roll made from the sheet also needs to tell which character made it and what it was for, so the history can show "Perception" next to the total and, later, list one character's rolls.

## Why one typed column per source

The only source today is a character. A campaign is the next one and comes with PRD-003, as a column of its own. A typed column can be indexed and filtered directly, and its meaning is in the schema; a generic `sourceType` / `sourceId` pair would mix ids of different tables in one column and leave its meaning to the code.

## Why a key rather than a label

The label is translated and follows the locale at display time. Storing the enum names keeps a stored roll readable whatever language the app runs in, and lets a renamed label apply to past rolls.

## Why the check lives in the domain

`RollContext` is part of `RecordedRoll`, so its check has to be typed in `shared/core`. `D20Check` carries the modifier rule of each check, which is then tested in the module that declares it.

## Why no foreign key

The history is global: a roll whose character was deleted is still a roll, and keeps its place in the log. Its `characterId` no longer matches anyone, so a filter by character simply does not find it.

## Consequences

- New `D20Check` (`SavingThrow`, `SkillCheck`, `Initiative`) in `character/domain`, with `modifier(character)`.
- `RollHistoryRepository.record(pool, context = null)`; the roller keeps calling `record(pool)`.
- A stored key this build does not know (a check added by a newer build, or a renamed enum) drops the context, not the roll.
- `AppDatabaseMigrationTest` gains a v6 witness and a v6 → v7 case.

## Alternatives considered

**A generic `sourceType` / `sourceId` pair**. Rejected: see above.

**The context as one JSON column**. Rejected: it cannot be indexed to filter by character.

**The translated label stored with the roll**. Rejected: it freezes the language of the moment the roll was made.

**A foreign key with `ON DELETE CASCADE`**. Rejected: deleting a character would silently rewrite a log meant to record what happened.
