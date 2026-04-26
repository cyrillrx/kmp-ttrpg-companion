# D&D 5e — Rules Reference

Game mechanics reference for D&D 5e (SRD 5.1 and 5.2).

This document contains rules that drive computed values in the app — modifier formulas, proficiency tables, class hit dice. It is distinct from translations ([`docs/i18n/srd-fr-en.md`](../i18n/srd-fr-en.md)) and architecture decisions ([`docs/adr/ADR-001-data-model.md`](../adr/ADR-001-data-model.md)).

---

## Ability Score Modifier

```
modifier = floor((score - 10) / 2)
```

| Score | Modifier |
|-------|----------|
| 1     | -5       |
| 2–3   | -4       |
| 4–5   | -3       |
| 6–7   | -2       |
| 8–9   | -1       |
| 10–11 | +0       |
| 12–13 | +1       |
| 14–15 | +2       |
| 16–17 | +3       |
| 18–19 | +4       |
| 20–21 | +5       |
| 22–23 | +6       |
| 24–25 | +7       |
| 26–27 | +8       |
| 28–29 | +9       |
| 30    | +10      |

---

## Proficiency Bonus

Applies identically to characters (by level) and creatures (by challenge rating).

| Level / CR | Proficiency Bonus |
|------------|-------------------|
| 0–4        | +2                |
| 5–8        | +3                |
| 9–12       | +4                |
| 13–16      | +5                |
| 17–20      | +6                |
| 21+        | +7                |

---

## Skill Check Modifier

```
skillModifier = abilityModifier                          (Proficiency.NONE)
              = abilityModifier + proficiencyBonus       (Proficiency.PROFICIENT)
              = abilityModifier + proficiencyBonus × 2   (Proficiency.EXPERT)
```

### Skill → Ability Mapping

| Skill           | Ability |
|-----------------|---------|
| Athletics       | STR     |
| Acrobatics      | DEX     |
| Sleight of Hand | DEX     |
| Stealth         | DEX     |
| Arcana          | INT     |
| History         | INT     |
| Investigation   | INT     |
| Nature          | INT     |
| Religion        | INT     |
| Animal Handling | WIS     |
| Insight         | WIS     |
| Medicine        | WIS     |
| Perception      | WIS     |
| Survival        | WIS     |
| Deception       | CHA     |
| Intimidation    | CHA     |
| Performance     | CHA     |
| Persuasion      | CHA     |

---

## Hit Die Per Class

A character's hit dice formula (e.g., `"6d8"` for a level-6 Cleric) is derived at runtime from `level × hitDie` — it is never stored.

| Class     | Hit Die |
|-----------|---------|
| Barbarian | d12     |
| Fighter   | d10     |
| Paladin   | d10     |
| Ranger    | d10     |
| Bard      | d8      |
| Cleric    | d8      |
| Druid     | d8      |
| Monk      | d8      |
| Rogue     | d8      |
| Warlock   | d8      |
| Sorcerer  | d6      |
| Wizard    | d6      |
