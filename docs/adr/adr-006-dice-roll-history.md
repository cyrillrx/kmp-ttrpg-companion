# ADR-006: Keep the dice roll history in a table of its own

**Status**: Accepted  
**Date**: 2026-11-01  
**Context**: [PRD-006](../prd/prd-006-dice.md) asks for every roll to be recorded, persisted across restarts, and capped at 500 entries.

## Decision

Each roll is one row of a new `DiceRoll` table, written as the roll is made.

- Three columns: an `AUTOINCREMENT` `id`, `rolledAt` (epoch milliseconds), and `roll`, the dice notation of the roll with the values each term rolled, such as `2d20kh1(15,10)+2d6(5,1)+1d4(3)+2`.
- A double roll records both its values; its rule tells which one is kept.
- The history is a single global log, ordered by `id`, newest first.
- The cap is enforced on write: the insert and the pruning of the rows beyond `MAX_RECORDED_ROLLS` run in one transaction.
- Schema v5 → v6 through `5.sqm`, one `CREATE TABLE`, and a regenerated snapshot `databases/6.db`.

## Context

The roller only kept the last roll on screen. The history needs every roll, in the order it was made, with each die's result (discarded ones included), the modifier and the time, and it has to survive a restart.

## Why the roll is stored as annotated notation

A roll holds one to `MAX_DICE_COUNT` dice, each a simple or a double roll. A child table would turn every read of the log into a join and every write into several inserts, for a list that is only ever read and written whole, so a roll stays one row. The dice notation is already the app's own description of a roll, read by the same parser as the user's input: storing it ties the format to the dice grammar rather than to the shape of the domain classes, and stripping the values gives back the expression to roll again.

Each term carries its own values, so a value never depends on its position in the whole roll: a change in how expressions order or merge their terms leaves the stored rows readable. The contract is the grammar's: it may grow, but a notation it reads today must keep its meaning.

## Why the order follows the id

`rolledAt` comes from the wall clock, which can go backwards (a clock change, a time zone sync). `AUTOINCREMENT` ids are never reused, so they give the order the rolls were recorded in, and the pruning can rely on them.

## Consequences

- New migration `sqldelight/…/cache/5.sqm` and snapshot `databases/6.db`; `AppDatabaseMigrationTest` gains a v5 witness and a v5 → v6 case.
- A stored roll that no longer reads as a valid one (a notation the parser refuses, a value out of range, a term whose values do not match its dice) is skipped and logged rather than failing the whole history.
- The view model records through a single consumer, so the rolls reach the table in the order they were made; that consumer runs in `detachedCommitScope()`, so a roll made as the roller closes is still written.
- A failed write is logged and does not affect the roll on screen.

## Alternatives considered

**A child table, one row per die**. Rejected: joins and multiple inserts for data that is never queried die by die.

**A JSON array of the dice**. Rejected: it ties the format to a dedicated serialized class kept in sync with the domain, when the notation already describes a roll.

**The notation and a flat list of values in two columns**. Rejected: the values would map to the dice by position, so a change in the canonical term order would silently shift the values of every stored roll.

**Keeping the history in memory**. Rejected: PRD-006 requires it to survive a restart.

**Pruning on read, or on a schedule**. Rejected: the table would grow unbounded between prunings; pruning in the write transaction keeps the cap an invariant.
