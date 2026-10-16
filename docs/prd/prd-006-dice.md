# PRD-006 — Virtual Dice

> **Status**: Draft | **Version**: 0.3 | **Last updated**: 2026-10-16

## Overview

The Virtual Dice module provides a fast, in-session dice rolling tool. It supports all standard TTRPG dice, cumulative multi-dice rolls with a modifier, advantage and disadvantage, and a roll history log.

## Goals

- Provide a reliable, glanceable dice roller usable mid-session without leaving the app.
- Support cumulative rolling (e.g. 2d6 + 1d4 + 3) with a running total.
- Roll any die with advantage or disadvantage, twice and keeping the higher or the lower, in one tap.
- Keep a history of recent rolls for reference and transparency.

## User Stories

| As a… | I want to…                                                | So that…                                                        |
|-------|-----------------------------------------------------------|-----------------------------------------------------------------|
| User  | Tap a die to roll it                                      | I get an instant result                                         |
| User  | Tap multiple dice to accumulate a total                   | I can resolve complex rolls (e.g. 3d6 + 1d4) in one view        |
| User  | Add a modifier to the total                               | I can apply an ability or proficiency bonus without mental sums |
| User  | Roll a die with advantage or disadvantage                 | I get the kept result without rolling twice and comparing       |
| User  | See the individual result of each die alongside the total | I can verify the roll                                           |
| User  | Remove one die from the current roll                      | I can fix a mistap without starting over                        |
| User  | Reset the current roll                                    | I can start a new roll without navigating away                  |
| User  | Browse my roll history                                    | I can refer back to a previous roll result                      |
| User  | Clear my roll history                                     | I can keep it relevant to the current session                   |

## Supported Dice

`d4`, `d6`, `d8`, `d10`, `d12`, `d20`, `d100`

A `d100` is a single roll from 1 to 100, not a pair of percentile `d10`.

## Functional Requirements

### Dice Rolling

- [ ] Display all seven dice types as tappable buttons.
- [ ] Tapping a die adds one roll of that die to the current roll pool.
- [ ] The current roll pool shows each individual die result and the running total.
- [ ] A **Reset** button clears the current roll pool.
- [ ] Rolling is instantaneous with a visual result (animation optional).

### Cumulative Rolling

- [ ] Multiple dice can be added to the same roll pool sequentially.
- [ ] Total is updated after each die is added or removed.
- [ ] Each die in the pool shows its individual result (e.g. d20: 14, d6: 3 → Total: 17).
- [ ] Any die can be removed from the pool on its own; the total follows.
- [ ] A modifier, set with **−** and **+** controls, is added to the total. It can be negative and defaults to 0.

### Advantage and Disadvantage

- [ ] A **Normal**, **Advantage** or **Disadvantage** mode can be selected; it stays active until changed.
- [ ] In Advantage or Disadvantage mode, adding any die rolls it twice and keeps the higher (advantage) or the lower (disadvantage).
- [ ] Both results are shown, the discarded one visibly set apart, and only the kept one counts toward the total.

### Roll History

- [ ] The current roll pool, modifier included, is persisted locally, so closing the app does not lose it.
- [ ] Resetting a non-empty pool records it in the history log. Resetting an empty pool records nothing.
- [ ] The log entry shows: dice rolled, individual results (discarded ones included), modifier, total, and timestamp.
- [ ] History is persisted locally across app restarts.
- [ ] The user can manually clear the entire history.
- [ ] History is a single global log, kept until cleared and capped at 500 entries: past the cap, the oldest entries are dropped.

### Entry Point

- [ ] The dice roller is reached from a section of the Home screen.

## Non-Functional Requirements

- Roll result must appear in under 100ms after tap.
- History must support at least 500 entries without degrading scroll performance.
- Results come from the platform's default PRNG (`kotlin.random.Random`); the source is injectable so tests can fix the outcome.
- The roll model lives in `shared/core`, so combat actions and monster hit dice (`2d8+4`) can reuse it. A roll gives a die and a value, nothing more; advantage and disadvantage read two rolls and keep one. Later rules that interpret a set of rolls are separate objects that read the rolls without altering them.
- The feature depends on no other feature's navigation, so it can later ship on its own (see Open Questions).

## Out of Scope

- Custom dice notation input (e.g. typing `3d8+5`) — may be added in a future iteration.
- Rules that interpret a set of rolls, such as roll-and-keep with a keep threshold (7th Sea 3rd edition) or counting only the 6s and the 1s on d6 (Free League games) — a future iteration, built on top of plain rolls.
- Dice with custom face labels (e.g. Fate/Fudge dice) — may be added when supporting other game systems.
- Sharing roll results with other players in real time.
- 3D dice animation.

## Open Questions

- How long should the roll history be retained? Options: session only, 7 days, indefinitely until manually cleared. **Settled**: until cleared, capped at 500 entries.
- Should the history be scoped per campaign session or be a single global log? **Settled** for now: a single global log, since campaigns do not exist yet; to revisit with [PRD-003 — Campaigns](prd-003-campaigns.md).
- Should there be a modifier field (e.g. +3 to add a bonus to the total)? **Settled**: yes, a −/+ modifier on the pool.
- Should the dice roller also ship as a standalone app? A second launcher activity in the same Android package gives it its own icon but shares the app's data and installation; a separate app needs its own application module on each target. Tracked as an idea to qualify under the mini-apps entry of the [roadmap](../roadmap.md) Exploration section.
