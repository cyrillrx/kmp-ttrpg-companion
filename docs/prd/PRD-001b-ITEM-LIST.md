# PRD-001b — Item List

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

Refer to [PRD-001 — Reference Data](PRD-001-REFERENCE-DATA.md) for the shared pattern, goals, and non-functional requirements.

## Filters

- Item type (Weapon, Armor, Potion, Wondrous Item, Tool, etc.)
- Rarity (Common, Uncommon, Rare, Very Rare, Legendary, Artifact)

## User Stories

| As a… | I want to…                              | So that…                                     |
|-------|-----------------------------------------|----------------------------------------------|
| User  | Browse and search the full item catalog | I can look up item stats and descriptions    |
| User  | Filter items by type and rarity         | I can find relevant loot for my session      |
| User  | Add items to a personal named list      | I can track loot or prepare a shop inventory |
| User  | Create a custom item                    | I can use homebrew items                     |
| User  | Export and import items                 | I can share custom items with others         |

## Functional Requirements

### Phase 1 — MVP (Offline)

- Display a scrollable list of all items from the local seed database.
- Full-text search on name and description.
- Filter by item type and rarity.
- View item detail: name, type, rarity, description, weight, value, attunement requirement.

### Phase 2 — Local Lists

- Create, edit, and delete personal named item lists stored locally.

### Phase 3 — Online Public Data

- Sync the item catalog from the public backend (read-only).

### Phase 4 — Advanced

- Sync personal item lists across devices.
- Create, edit, and delete custom items.
- Export an item as a shareable format; import an item shared by another user.
