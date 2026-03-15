# PRD-000 — Vision & Foundation

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

## Overview

TTRPG Companion is a cross-platform mobile and desktop application designed to support tabletop role-playing game sessions.
It targets both Game Masters (GMs) and players, providing tools to manage campaigns, characters, rules references, and in-session utilities — all in one place.

## Problem Statement

Managing a TTRPG session involves juggling a large amount of information: spell lists, character sheets, monster stats, campaign notes, inventory. Physical books and scattered notes are slow and error-prone. Existing digital tools are either too specialized, not available offline, or require an internet connection and account from the start.

## Goals

- Provide a companion app usable from day one with no account required (offline-first).
- Enable seamless data sync and sharing between devices and players when online.
- Support D&D 5e as the primary rule system, with extensibility to others.
- Keep the experience simple and fast during a live session.

## User Roles

Roles are **not fixed account types** — they are **per-campaign roles** determined by how a user relates to a specific campaign. The same user can be a GM in one campaign and a Player in another.

| Role        | Abbreviation | How it is assigned                                                                             |
|-------------|--------------|------------------------------------------------------------------------------------------------|
| Game Master | GM           | Automatically assigned to the user who **creates** the campaign.                               |
| Co-GM       | Co-GM        | Assigned when a user is **invited as a co-GM** or **promoted** by a GM.                        |
| Player      | P            | Assigned when a user **joins a campaign by invitation**.                                       |
| User        | U            | Any user, regardless of their role in any campaign. Applies to features available to everyone. |

### Campaign Role Assignment

| Situation                                       | Role in that campaign                                                     |
|-------------------------------------------------|---------------------------------------------------------------------------|
| User creates a campaign                         | GM / owner (full access)                                                  |
| User is invited as co-GM, or promoted by the GM | Co-GM (same as GM, except cannot delete the campaign or remove the owner) |
| User is invited as a player                     | Player (read-only, except own PC sheets)                                  |

### Access Matrix

| Action                                               | Any User (U) | GM (owner) | Co-GM | Player |
|------------------------------------------------------|--------------|------------|------|-----|
| Read public reference data (spells, items, monsters) | ✅            | ✅          | ✅    | ✅   |
| Create/edit own character sheets (personal library)  | ✅            | ✅          | ✅    | ✅   |
| Create a campaign                                    | ✅            | ✅          | ✅    | ✅   |
| Edit campaign content (notes, roster, settings)      | —            | ✅          | ✅    | ✗   |
| View secret folders and items                        | —            | ✅          | ✅    | ✗   |
| Read visible campaign content (notes, roster)        | —            | ✅          | ✅    | ✅   |
| Invite players or co-GMs                             | —            | ✅          | ✅    | ✗   |
| Promote / demote members                             | —            | ✅          | ✅     | ✗   |
| Edit own PC sheet within a campaign                  | —            | ✅          | ✅    | ✅   |
| Edit other characters' sheets within a campaign      | —            | ✅          | ✅    | ✗   |
| Delete the campaign                                  | —            | ✅          | ✗    | ✗   |
| Remove the GM (owner) from the campaign              | —            | ✗          | ✗    | ✗   |

## Connectivity Phases

The application is designed to be rolled out in three phases of increasing connectivity:

### Phase 1 — Offline / Local
- All data is stored locally on the device.
- A pre-loaded seed database provides reference content (spells, items, monsters).
- Users can create and manage their own content locally.

### Phase 2 — Online, Public Data
- Connection to a backend server to fetch public, immutable reference data.
- Examples: official spell lists, item catalogs, monster stat blocks.
- No user account required. Read-only sync of public content.

### Phase 3 — Online, Connected with Sync
- User authentication (registration and login).
- Data synchronization across devices.
- Campaign sharing and access management (editor, reader, no access).
- Role management within a shared campaign.

## Supported Game Systems

The app will launch with **D&D 5e** as the primary supported system, including compatible systems that share its core mechanics (e.g. Pathfinder, Starfinder).

Additional game systems may be added in the future. The list is intentionally open-ended and will be prioritized based on demand and community feedback. Each new system will be scoped in a dedicated PRD when the time comes.

## Application Structure

The app is organized around three main sections accessible from the home screen:

```
Home
├── Campaign
│   ├── CampaignList
│   ├── CampaignCreate
│   └── CampaignDetail
│       ├── NoteList / NoteCreate
│       └── CharacterSheetList / CharacterSheetSelector
├── CharacterSheet
│   ├── CharacterSheetList
│   ├── CharacterSheetCreate
│   └── CharacterSheetDetail
└── Rules
    ├── SpellList / SpellDetail
    ├── ItemList / ItemDetail
    └── MonsterList / MonsterDetail
```

## Non-Functional Requirements

- **Offline-first**: core features must work without a network connection.
- **Cross-platform**: Android, iOS, and Desktop (via Kotlin Multiplatform + Compose Multiplatform).
- **Performance**: list screens must load within 300ms on mid-range devices.
- **Data safety**: user-created content must never be lost due to a sync conflict; conflicts must be resolved non-destructively.
