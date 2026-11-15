# PRD-000 — Vision & Foundation

> **Status**: Draft | **Version**: 0.3 | **Last updated**: 2026-11-15

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
|------------------------------------------------------|--------------|------------|-------|--------|
| Read public reference data (spells, items, monsters) | ✅            | ✅          | ✅     | ✅      |
| Create/edit own character sheets (personal library)  | ✅            | ✅          | ✅     | ✅      |
| Create a campaign                                    | ✅            | ✅          | ✅     | ✅      |
| Edit campaign content (notes, roster, settings)      | —            | ✅          | ✅     | ✗      |
| View secret folders and items                        | —            | ✅          | ✅     | ✗      |
| Read visible campaign content (notes, roster)        | —            | ✅          | ✅     | ✅      |
| Invite players or co-GMs                             | —            | ✅          | ✅     | ✗      |
| Promote / demote members                             | —            | ✅          | ✅     | ✗      |
| Edit own PC sheet within a campaign                  | —            | ✅          | ✅     | ✅      |
| Edit other characters' sheets within a campaign      | —            | ✅          | ✅     | ✗      |
| Delete the campaign                                  | —            | ✅          | ✗     | ✗      |
| Remove the GM (owner) from the campaign              | —            | ✗          | ✗     | ✗      |

## Connectivity Phases

The application is designed to be rolled out in two phases of increasing connectivity:

### Phase 1 — Offline / Local
- All data is stored locally on the device.
- A pre-loaded seed database provides reference content (spells, items, monsters).
- Users can create and manage their own content locally.
- The reference content stays on the device: the whole compendium fits there, so serving it would only add network calls. A compendium served by the backend, probably mixed with the local one, becomes worth it if the content needs regular updates, if users add and share their own content (privately or as UGC), or if a legal constraint limits what the app may bundle beyond the SRD.

### Phase 2: Online, Connected with Sync
- Not tied to a point of the functional roadmap: an online feature lands, where it has one, once its local counterpart exists.
- User authentication (registration and login).
- Data synchronization across devices.
- Campaign sharing and access management (GM, co-GM, player).
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
- **Accessibility**: screens must stay usable with a large system font and keep touch targets of at least 48dp. Screen-reader support (TalkBack, VoiceOver) is best effort and not a goal for now: a missing or redundant description is not worth a fix of its own.
