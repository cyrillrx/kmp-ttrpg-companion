# ADR-003: Character Model — Single Class for PC and NPC

**Status**: Accepted  
**Date**: 2026-05-09  
**Context**: Phase 5 — Character sheets design, before implementation begins.

---

## Decision

`PlayerCharacter` is renamed to `Character`. There is no separate `Npc` subtype.

The hierarchy becomes:

```
Creature (abstract)
├── Monster     → compendium, read-only, with CR / type / immunities / translations
└── Character   → mutable, saved, user-managed
```

`Character` carries all stat fields (abilities, AC, HP, speeds, languages inherited from `Creature`; plus `name`, `description`, `level`, `clazz`, `initiativeModifier`, `skills`, `proficiencyBonus`).

The `Class` enum gains an `UNKNOWN` value to represent characters without a defined class (typical for NPCs).

---

## Context

Before this ADR, the only mutable character entity was `PlayerCharacter`, leaving no model for NPCs. Two design questions had to be resolved:

1. **Should NPC be a separate subtype of `Character`?**
2. **Where does ownership (GM vs. Player) live?**

---

## Why no `Npc` subtype

NPCs and PCs are structurally identical: same base stats, same fields, same data needs. The only observable differences are:

- Some PC fields (level, class) may be left at default values or `UNKNOWN` for an NPC.
- Ownership: the GM controls NPCs; the GM and/or a player control PCs.
- Conversion: an NPC can become a PC (e.g., a player joins a one-shot with a pre-made character). This conversion is trivial when the models are identical.

Introducing a subtype would create code duplication with no structural difference, and would make NPC→PC conversion unnecessarily complex. A single `Character` class with `Class.UNKNOWN` handles the NPC case without sacrificing type safety.

---

## Why ownership is not on `Character`

Ownership (who can read and edit a character) is a campaign-level concern, not a character-level one. The same character sheet may be:

- Privately owned (offline, no campaign)
- Attached to a campaign as a PC (editable by the player)
- Attached to a campaign as an NPC (editable by the GM only)

These roles will be represented as a join between `User` and `Character` at the campaign layer (see PRD-003). Adding a `role` or `isNpc` flag to `Character` would conflate data with access control.

---

## Consequences

- `PlayerCharacter.kt` → `Character.kt`; all references updated.
- `PlayerCharacterRepository`, `PlayerCharacterFilter`, `RamPlayerCharacterRepository` renamed accordingly.
- `Character.Class.UNKNOWN` is not displayed in the spell class filter UI (filtered out from `entries`).
- `Spell.availableClasses: List<Character.Class>` — the reference from the Spell domain to Character is unchanged in intent.
- ADR-001 section 8 and 9 updated: references to `PlayerCharacter` replaced with `Character`.

---

## Alternatives considered

**Option: `Npc extends Character` sibling of `PlayerCharacter`**  
Rejected: structural identity means the subtype would carry no additional fields initially, adding complexity for no benefit. Fields specific to NPCs (optional CR, type) can be added to `Character` as nullable properties when the need arises.

**Option: `isNpc: Boolean` flag on `PlayerCharacter`**  
Rejected: semantic confusion, hard to enforce invariants, ownership and role are not character-level data.
