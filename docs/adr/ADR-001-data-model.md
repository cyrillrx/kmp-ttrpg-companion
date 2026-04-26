# ADR-001: Compendium Data Model Decisions

**Status**: Accepted  
**Date**: 2026-04-26  
**Context**: Phase 4 intermediate step — data quality & coherence, before backend consumption by clients.

---

## 1. Entity + Translations Pattern

### Decision
All compendium entities separate mechanical data from locale-specific text using a `translations` map.

```json
{
  "id": "acid-splash",
  "source": "srd_5.1",
  "level": 0,
  "school": "conjuration",
  "translations": {
    "fr": {
      "name": "Aspersion acide",
      "description": "<p>...</p>"
    }
  }
}
```

### Rationale
- Mechanical fields (level, school, CR, ability scores) are locale-independent and must not be duplicated per language.
- Text fields (name, description, castingTime, range, duration) vary by language and must be cleanly separated for contributors to add translations.
- The app is open-source: a contributor adding English translations should only need to add an `"en"` block, not restructure the entire entry.
- This pattern is DB-ready: maps cleanly to a `spell` table + `spell_translation` table.

### Translation Resolution Order
Applied consistently in all repository implementations (KMP, backend):

1. Requested locale (e.g., `"fr"`)
2. Fallback to `"en"` if requested locale is absent
3. If `"en"` is also absent, pick the first available locale sorted alphabetically
4. If `translations` is empty → entity is malformed; log a warning and skip

### i18n in Detail Views
The API exposes the full `translations` map. Detail views display the active locale and allow the user to switch between available locales without a new network request.

---

## 2. Source Field

### Decision
Every entity has a `source` field — a single string identifying its origin.

```json
"source": "srd_5.1"
```

### Known Values
| Value       | Description                             |
|-------------|-----------------------------------------|
| `srd_5.1`   | D&D 5e SRD 2014                         |
| `srd_5.2`   | D&D 5e SRD 2024                         |
| `phb_2014`  | Player's Handbook 2014                  |
| `phb_2024`  | Player's Handbook 2024                  |
| `tasha`     | Tasha's Cauldron of Everything          |
| `xanathar`  | Xanathar's Guide to Everything          |
| `fateforge` | Fateforge (Studio Agate)                |
| `custom`    | User-created content                    |

### Rationale
- A single string is the simplest model that supports filtering by source and distinguishing official from user-created content.
- Official supplements and core books are treated the same way — no distinction needed between "official SRD" and "supplement".
- `isCustom: true` (present in the current raw data) is replaced by `source: "custom"`.
- The model can be enriched later (e.g., adding author identity for UGC) without breaking existing data.

---

## 3. Lowercase English Enum Values in JSON

### Decision
All enum values in JSON source files and API responses use **lowercase English with underscores** (e.g., `"conjuration"`, `"wondrous_item"`, `"neutral_evil"`).

### Rationale
- Lowercase is idiomatic in JSON APIs and easier to read.
- English is the canonical language for all mechanical keys and values — French content is confined to `translations.fr`.
- Enum names in code (Kotlin, Rust, Go) remain `UPPER_CASE` as per each language's convention; the serialization layer handles case mapping transparently.

### Serialization Convention Per Language
- **Kotlin**: `@SerialName("conjuration")` on each enum entry, or a custom serializer
- **Rust**: `#[serde(rename_all = "snake_case")]` on the enum
- **Go**: lowercase string in JSON struct tags

### Reference
Complete FR↔EN mapping tables: [`docs/i18n/srd-fr-en.md`](../i18n/srd-fr-en.md)

---

## 4. Proficiency-Based Modifier Computation

### Decision
Numeric modifiers (skill check bonuses, saving throw bonuses) are **always computed at runtime**, never stored.

### Formula
```
abilityModifier  = floor((abilityScore - 10) / 2)
proficiencyBonus = derived from level (characters) or challengeRating (creatures)

skillModifier    = abilityModifier                          (Proficiency.NONE)
                 = abilityModifier + proficiencyBonus       (Proficiency.PROFICIENT)
                 = abilityModifier + proficiencyBonus × 2   (Proficiency.EXPERT)
```

Proficiency bonus table (game rule): see [`docs/rules/dnd-5e.md`](../rules/dnd-5e.md).

### Rationale
- Storing pre-computed modifiers duplicates data derivable from ability scores and proficiency level.
- Pre-computed values become inconsistent if scores change (custom creatures, character progression).
- This matches the D&D 5e SRD design.

---

## 5. Saving Throw Proficiency Embedded in Ability

### Decision
Each `Ability` holds both the raw score and the saving throw proficiency for that ability. There is no separate `SavingThrows` class.

```kotlin
class Ability(
    val value: Int,
    val savingThrowProficiency: Proficiency = Proficiency.NONE,
) {
    val modifier: Int = getModifier(value)
    // savingThrowModifier = modifier + proficiencyBonus (caller-provided)
}
```

### Rationale
- A saving throw is intrinsically tied to its ability (STR save uses STR modifier). Co-locating proficiency with the ability makes this relationship explicit.
- Avoids a redundant parallel structure (an `Abilities` object + a `SavingThrows` object with identical keys).
- Simplifies serialization: each ability entry in JSON carries both its score and its saving throw proficiency.

---

## 6. Bounded Object Pattern for Skills

### Decision
`Skills` is a data class with all 18 D&D 5e skills as **named properties** typed as `Proficiency`. No dynamic map or list.

```kotlin
data class Skills(
    val acrobatics: Proficiency = Proficiency.NONE,
    val arcana: Proficiency = Proficiency.NONE,
    // ... all 18 skills
)
```

### Rationale
- The set of skills in D&D 5e is bounded and fixed (18 skills). A dynamic structure is unnecessary and error-prone.
- Named properties give compile-time safety: a typo in a skill name is a compile error, not a silent null.
- DB-ready: maps directly to columns without requiring a join table.

Skill → ability mapping (game rule, not architecture): see [`docs/rules/dnd-5e.md`](../rules/dnd-5e.md).

---

## 7. DamageAffinity Enum for Resistances and Immunities

### Decision
Damage affinities use a structured object with each damage type as a **named property** typed as `DamageAffinity`. Condition immunities use the same bounded pattern with `Boolean` properties.

```kotlin
enum class DamageAffinity { NONE, RESISTANT, IMMUNE, VULNERABLE }

data class DamageAffinities(
    val acid: DamageAffinity = DamageAffinity.NONE,
    val bludgeoning: DamageAffinity = DamageAffinity.NONE,
    val cold: DamageAffinity = DamageAffinity.NONE,
    val fire: DamageAffinity = DamageAffinity.NONE,
    val force: DamageAffinity = DamageAffinity.NONE,
    val lightning: DamageAffinity = DamageAffinity.NONE,
    val necrotic: DamageAffinity = DamageAffinity.NONE,
    val piercing: DamageAffinity = DamageAffinity.NONE,
    val poison: DamageAffinity = DamageAffinity.NONE,
    val psychic: DamageAffinity = DamageAffinity.NONE,
    val radiant: DamageAffinity = DamageAffinity.NONE,
    val slashing: DamageAffinity = DamageAffinity.NONE,
    val thunder: DamageAffinity = DamageAffinity.NONE,
    val bludgeoningNonMagical: DamageAffinity = DamageAffinity.NONE,
    val piercingNonMagical: DamageAffinity = DamageAffinity.NONE,
    val slashingNonMagical: DamageAffinity = DamageAffinity.NONE,
)

data class ConditionImmunities(
    val blinded: Boolean = false,
    val charmed: Boolean = false,
    val deafened: Boolean = false,
    val exhaustion: Boolean = false,
    val frightened: Boolean = false,
    val grappled: Boolean = false,
    val incapacitated: Boolean = false,
    val paralyzed: Boolean = false,
    val petrified: Boolean = false,
    val poisoned: Boolean = false,
    val prone: Boolean = false,
    val restrained: Boolean = false,
    val stunned: Boolean = false,
    val unconscious: Boolean = false,
)
```

### Rationale
- A free list of strings allows contradictions (a creature cannot be both resistant and immune to the same damage type).
- A single enum value per damage type is unambiguous.
- DB-ready: each damage type maps to a column.

---

## 8. currentHitPoints Belongs Outside the Compendium Model

### Decision
`currentHitPoints` is **not** part of `Creature` or any compendium entity.

### Rationale
- Compendium entities are **reference data**: they describe a creature type (template), not a specific instance.
- A single encounter may have multiple instances of the same creature type, each with independent current HP.
- Tracking current HP belongs in a future **combat tracker** feature, using a `CombatInstance` entity referencing a `Creature` by ID.
- For `PlayerCharacter` (Phase 5), `currentHitPoints` is appropriate because there is one persistent instance per character.

---

## 9. hitDice on Creature Only, Not BaseCreature

### Decision
`hitDice` (e.g., `"6d8+6"`) is a field on `Creature` only. It is **not** on `BaseCreature` and **not** on `PlayerCharacter`.

For `PlayerCharacter`, hit dice are computed from `level` and `clazz.hitDie` — never stored.

### Rationale
- Storing `hitDice` on `PlayerCharacter` would create a redundant source of truth alongside `level` and `class`, with risk of inconsistency.
- For creatures, `hitDice` cannot be derived (there is no fixed "class" for monsters), so it must be stored explicitly.
- The D&D 5e SRD displays stat blocks as `"33 (6d8+6)"` — `maxHitPoints` is the average, `hitDice` is the formula used when rolling HP.

Hit die per class (game rule): see [`docs/rules/dnd-5e.md`](../rules/dnd-5e.md).
