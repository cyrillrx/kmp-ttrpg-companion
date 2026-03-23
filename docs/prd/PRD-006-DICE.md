# PRD-006 — Virtual Dice

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

## Overview

The Virtual Dice module provides a fast, in-session dice rolling tool. It supports all standard TTRPG dice, cumulative multi-dice rolls, and a roll history log.

## Goals

- Provide a reliable, glanceable dice roller usable mid-session without leaving the app.
- Support cumulative rolling (e.g. 2d6 + 1d4) with a running total.
- Keep a history of recent rolls for reference and transparency.

## User Stories

| As a… | I want to…                                                | So that…                                                 |
|-------|-----------------------------------------------------------|----------------------------------------------------------|
| User  | Tap a die to roll it                                      | I get an instant result                                  |
| User  | Tap multiple dice to accumulate a total                   | I can resolve complex rolls (e.g. 3d6 + 1d4) in one view |
| User  | See the individual result of each die alongside the total | I can verify the roll                                    |
| User  | Reset the current roll                                    | I can start a new roll without navigating away           |
| User  | Browse my roll history                                    | I can refer back to a previous roll result               |
| User  | Clear my roll history                                     | I can keep it relevant to the current session            |

## Supported Dice

`d4`, `d6`, `d8`, `d10`, `d12`, `d20`, `d100`

## Functional Requirements

### Dice Rolling

- [ ] Display all seven dice types as tappable buttons.
- [ ] Tapping a die adds one roll of that die to the current roll pool.
- [ ] The current roll pool shows each individual die result and the running total.
- [ ] A **Reset** button clears the current roll pool.
- [ ] Rolling is instantaneous with a visual result (animation optional).

### Cumulative Rolling

- [ ] Multiple dice can be added to the same roll pool sequentially.
- [ ] Total is updated after each die is added.
- [ ] Each die in the pool shows its individual result (e.g. d20: 14, d6: 3 → Total: 17).

### Roll History

- [ ] Each completed roll (before reset) is recorded in a history log.
- [ ] The log entry shows: dice rolled, individual results, total, and timestamp.
- [ ] History is persisted locally across app restarts.
- [ ] The user can manually clear the entire history.
- [ ] History retention policy: TBD (see Open Questions).

## Non-Functional Requirements

- Roll result must appear in under 100ms after tap.
- History must support at least 500 entries without degrading scroll performance.
- Dice results must be cryptographically random or use a high-quality PRNG to ensure fairness.

## Out of Scope

- Custom dice notation input (e.g. typing `3d8+5`) — may be added in a future iteration.
- Dice with custom face labels (e.g. Fate/Fudge dice) — may be added when supporting other game systems.
- Sharing roll results with other players in real time.
- 3D dice animation.

## Open Questions

- How long should the roll history be retained? Options: session only, 7 days, indefinitely until manually cleared.
- Should the history be scoped per campaign session or be a single global log?
- Should there be a modifier field (e.g. +3 to add a bonus to the total)?
