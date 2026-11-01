# ADR-006: Keep the dice roll history in a table of its own

**Status**: Accepted  
**Date**: 2026-11-01  
**Context**: [PRD-006](../prd/prd-006-dice.md) asks for every roll to be recorded, persisted across restarts, and capped at 500 entries.

## Decision

Each roll is one row of a new `DiceRoll` table, written as the roll is made.

- Four columns: an `AUTOINCREMENT` `id`, `rolledAt` (epoch milliseconds), `modifier`, and `rolls`, the dice of the roll as a JSON array of `StoredDieRoll`.
- A `StoredDieRoll` holds the die and its kept value; a roll with advantage or disadvantage adds the `discarded` value and its `rule`. Both are set together, or neither.
- The history is a single global log, ordered by `id`, newest first.
- The cap is enforced on write: the insert and the pruning of the rows beyond `MAX_RECORDED_ROLLS` run in one transaction.
- Schema v5 → v6 through `5.sqm`, one `CREATE TABLE`, and a regenerated snapshot `databases/6.db`.

## Context

The roller only kept the last roll on screen. The history needs every roll, in the order it was made, with each die's result (discarded ones included), the modifier and the time, and it has to survive a restart.

## Why the dice are stored as JSON

A roll holds one to `MAX_DICE_COUNT` dice, each a simple or a double roll. A child table would turn every read of the log into a join and every write into several inserts, for a list that is only ever read and written whole. A JSON column keeps a roll one row. The format is owned by `StoredDieRoll` rather than by the domain types, so a domain refactor does not silently change what is on disk.

## Why the order follows the id

`rolledAt` comes from the wall clock, which can go backwards (a clock change, a time zone sync). `AUTOINCREMENT` ids are never reused, so they give the order the rolls were recorded in, and the pruning can rely on them.

## Consequences

- New migration `sqldelight/…/cache/5.sqm` and snapshot `databases/6.db`; `AppDatabaseMigrationTest` gains a v5 witness and a v5 → v6 case.
- A stored roll that no longer reads as a valid one (a die removed in a later version, a value out of range, a double roll missing half of its values) is skipped and logged rather than failing the whole history.
- The view model records through a single consumer, so the rolls reach the table in the order they were made; that consumer runs in `detachedCommitScope()`, so a roll made as the roller closes is still written.
- A failed write is logged and does not affect the roll on screen.

## Alternatives considered

**A child table, one row per die**: rejected, joins and multiple inserts for data that is never queried die by die.

**Keeping the history in memory**: rejected, PRD-006 requires it to survive a restart.

**Pruning on read, or on a schedule**: rejected, the table would grow unbounded between prunings; pruning in the write transaction keeps the cap an invariant.
