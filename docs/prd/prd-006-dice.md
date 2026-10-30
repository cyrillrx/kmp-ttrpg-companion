# PRD-006 — Virtual Dice

> **Status**: Draft | **Version**: 0.5 | **Last updated**: 2026-10-18

## Overview

The Virtual Dice module provides a fast, in-session dice rolling tool. It supports all standard TTRPG dice, composed multi-dice rolls with a modifier, direct d20 rolls with advantage and disadvantage, and a roll history log.

## Goals

- Provide a reliable, glanceable dice roller usable mid-session without leaving the app.
- Compose a roll (e.g. 2d6 + 1d4 + 3) and throw it at once, then throw it again in one tap.
- Roll a d20 with advantage or disadvantage, twice and keeping the higher or the lower, in one tap.
- Keep a history of recent rolls for reference and transparency.

## User Stories

| As a… | I want to…                                                | So that…                                                        |
|-------|-----------------------------------------------------------|-----------------------------------------------------------------|
| User  | Roll a d20 in one tap                                     | I resolve an attack, a check or a save at once                  |
| User  | Roll a d20 with advantage or disadvantage                 | I get the kept result without rolling twice and comparing       |
| User  | Compose several dice before rolling them                  | I can throw a complex roll (e.g. 3d6 + 1d4) at once             |
| User  | Remove one die from the composition                       | I can fix a mistap before rolling                               |
| User  | Add a modifier to the total                               | I can apply an ability or proficiency bonus without mental sums |
| User  | Roll the same composition again                           | I can repeat an attack's damage without composing it anew       |
| User  | See the individual result of each die alongside the total | I can verify the roll                                           |
| User  | Clear the composition                                     | I can start a new roll without navigating away                  |
| User  | Browse my roll history                                    | I can refer back to a previous roll result                      |
| User  | Clear my roll history                                     | I can keep it relevant to the current session                   |

## Supported Dice

`d4`, `d6`, `d8`, `d10`, `d12`, `d20`, `d100`

A `d100` is a single roll from 1 to 100, not a pair of percentile `d10`.

## Functional Requirements

### Direct d20 Rolls

- [x] A **d20**, an **Advantage** and a **Disadvantage** action each roll at once, with the current modifier; a d20 test only ever adds a bonus, never other dice.
- [x] **Advantage** and **Disadvantage** roll a d20 twice and keep the higher (advantage) or the lower (disadvantage); there is no mode to remember.
- [x] Both results are shown, the discarded one visibly set apart, and only the kept one counts toward the total.
- [x] A direct roll leaves the composition untouched.

### Composing a Roll

- [x] All seven dice can be added to a composition, each showing how many of it are composed; one die of a type can be removed again.
- [x] A modifier, set with **−** and **+** controls and shared with direct rolls, is added to the total. It can be negative and defaults to 0.
- [x] A **Roll** action, showing the composition's notation, throws every composed die at once. It is disabled while nothing is composed.
- [x] The composition stays after a roll, so the same roll can be thrown again in one tap.
- [x] A **Clear** key in the pad empties the composition; the modifier, shared with direct rolls, and the last roll stay.
- [x] The last roll shows each individual die result, grouped by die, alongside its notation and total. It is read-only: a die is never added to or removed from a roll already thrown.
- [x] Rolling is instantaneous with a visual result (animation optional).
- [x] The composition and the modifier last while the app runs; the roller opens empty when the app starts.

### Roll History

- [x] Each roll, composed or direct, is recorded in the history log.
- [ ] The log entry shows: dice rolled, individual results (discarded ones included), modifier, total, and timestamp.
- [x] History is persisted locally across app restarts.
- [ ] The user can manually clear the entire history.
- [x] History is a single global log, kept until cleared and capped at 500 entries: past the cap, the oldest entries are dropped.

### Dice Notation

- [x] A notation such as `2d6 + 1d4 + 3` reads into a dice expression and writes back; spaces around a sign are ignored but a space inside a term is refused, `d` and `D` are both accepted, and `d20` stands for `1d20`.
- [x] An expression holds at most 100 dice in total; a notation asking for more is refused.
- [x] An expression's modifier stays within ±1000, enough for 100 dice with a +10 bonus each; a notation beyond it is refused.
- [x] A notation that cannot be read is refused with its reason — blank, a sign with no term after it, zero dice, subtracted dice, an unsupported die, no dice at all, too many dice, a modifier out of range, or a part that is neither dice nor a number — and is never silently corrected. A refusal for going past a limit names that limit.

### Entry Point

- [x] The dice roller is reached from a section of the Home screen.

## Non-Functional Requirements

- Roll result must appear in under 100ms after tap.
- History must support at least 500 entries without degrading scroll performance.
- Results come from the platform's default PRNG (`kotlin.random.Random`); the source is injectable so tests can fix the outcome.
- The roll model lives in `shared/core`, so combat actions and monster hit dice (`2d8+4`) can reuse it. A roll gives a die and a value, nothing more; advantage and disadvantage read two rolls and keep one. Later rules that interpret a set of rolls are separate objects that read the rolls without altering them.
- The feature depends on no other feature's navigation, so it can later ship on its own (see Open Questions).

## Out of Scope

- Advantage and disadvantage on dice other than the d20 — the roll model already supports any die, so it only needs a control if a game calls for it.
- Typing a dice notation in the roller (e.g. `3d8+5`) — may be added in a future iteration, on top of the notation reader.
- Finding dice notations inside free text, for instance to make them tappable on an item sheet — a future step built on the notation reader.
- Rules that interpret a set of rolls, such as roll-and-keep with a keep threshold (7th Sea 3rd edition) or counting only the 6s and the 1s on d6 (Free League games) — a future iteration, built on top of plain rolls.
- Dice with custom face labels (e.g. Fate/Fudge dice) — may be added when supporting other game systems.
- Sharing roll results with other players in real time.
- 3D dice animation.

## Open Questions

- How long should the roll history be retained? Options: session only, 7 days, indefinitely until manually cleared. **Settled**: until cleared, capped at 500 entries.
- Should the history be scoped per campaign session or be a single global log? **Settled** for now: a single global log, since campaigns do not exist yet; to revisit with [PRD-003 — Campaigns](prd-003-campaigns.md).
- Should there be a modifier field (e.g. +3 to add a bonus to the total)? **Settled**: yes, a −/+ modifier shared by direct rolls and the composition.
- Should the composition survive an app restart? **Settled**: no. Reopening the app on the dice left from a previous session felt confusing for a dice roller; the history log is what keeps past rolls.
- Should the dice roller also ship as a standalone app? A second launcher activity in the same Android package gives it its own icon but shares the app's data and installation; a separate app needs its own application module on each target. Tracked as an idea to qualify under the mini-apps entry of the [roadmap](../roadmap.md) Exploration section.
- Should a d100 read as a tens die and a units die (00–90 plus 0–9), as rolled at the table, rather than the single number described under Supported Dice? Its rendering is to explore.
