# PRD-001b — Item List

> **Status**: Draft | **Version**: 0.2 | **Last updated**: 2026-11-13

Refer to [PRD-001 — Reference Data](prd-001-reference-data.md) for the shared pattern, goals, and non-functional requirements.

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

- [x] Display a scrollable list of all items from the local seed database.
- [x] Full-text search on name and description.
- [x] Filter by item type and rarity.
- [x] View item detail: name, type, rarity, description, value, attunement requirement.

### Phase 2 — Local Lists

- [x] Create, edit, and delete personal named item lists stored locally.

### Phase 3: Custom entries, export and import

- [ ] Create, edit, and delete custom items.
- [ ] Export an item as a shareable format; import an item shared by another user.

### Online

> The catalog itself stays on the device (see [PRD-000](prd-000-vision.md)).

- [ ] Sync personal item lists across devices.
- [ ] Sync custom items across devices.
