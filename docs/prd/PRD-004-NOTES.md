# PRD-004 — Notes

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

## Overview

The Notes module allows users to create, browse, search, and manage free-form personal notes. It is **game-system-agnostic** and follows the same list/detail/CRUD pattern as the reference data modules (PRD-001).
Notes are standalone entities at this stage; attaching them to a campaign is a later-phase cross-entity relationship (see PRD-003).

The Notes module also serves as the foundation for the Generators module (PRD-005): generated content (NPCs, names, plants, etc.) is saved as notes and managed through the same pattern.

## Goals

- Provide a simple, fast note-taking tool usable during any session, regardless of game system.
- Keep notes fully accessible offline.
- Establish the base list/search/CRUD pattern reused by the Generators module.

## User Stories

| As a… | I want to…                          | So that…                                         |
|-------|-------------------------------------|--------------------------------------------------|
| User  | Create a note with a title and body | I can write down anything relevant to my session |
| User  | Browse and search my notes          | I can quickly find a note I wrote previously     |
| User  | Edit a note                         | I can update or correct its content              |
| User  | Delete a note                       | I can remove notes I no longer need              |

**Later phase — Campaign integration (see PRD-003)**

| As a… | I want to…                    | So that…                                                      |
|-------|-------------------------------|---------------------------------------------------------------|
| User  | Attach a note to a campaign   | I can organize session notes within their campaign context    |
| User  | Detach a note from a campaign | The note returns to my personal library without being deleted |

## Functional Requirements

### Phase 1 — MVP (Offline)

- Create a note with a title and a free-text body.
- List all personal notes, sorted by last modified date (most recent first).
- Full-text search on title and body.
- Edit and delete notes.
- Support linking to an external URL as an alternative to a written body (e.g. a Google Doc, Notion page).

### Phase 3 — Campaign integration

- Attach an existing note to a campaign (see PRD-003).
- Notes remain in the user's personal library even when detached from a campaign.

## Non-Functional Requirements

- Notes must load and save instantly on-device (no perceptible delay).
- The module must work fully offline.

## Out of Scope

- Rich text formatting (bold, lists, headings) — may be considered in a future iteration.
- Sharing notes directly between users outside of a campaign context.

## Open Questions

- Should notes support tags or categories for personal organisation?
- Should there be a maximum note length?
