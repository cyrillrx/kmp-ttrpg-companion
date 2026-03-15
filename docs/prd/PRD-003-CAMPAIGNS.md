# PRD-003 — Campaigns

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

## Overview

The Campaign module allows a GM to create and manage a campaign, attach resources (characters, monsters, notes), and — when online — share the campaign with players with granular access control.

## Goals

- Give GMs a central workspace to organize everything related to a campaign.
- Allow attaching and navigating all campaign resources from a single screen.
- Enable sharing a campaign with players and controlling their level of access.
- Allow GMs to keep some notes and resources secret from players.

## User Stories

| As a…  | I want to…                                               | So that…                                                              |
|--------|----------------------------------------------------------|-----------------------------------------------------------------------|
| GM     | Create a campaign with a name and description            | I have a central place to organize my session content                 |
| GM     | Edit and delete my campaigns                             | I can keep my campaign list clean                                     |
| GM     | Attach characters, NPCs, and monsters to a campaign      | I can access all relevant entities quickly during play                |
| GM     | Share a campaign with specific players                   | My players can access their characters and campaign info              |
| GM     | Invite another user as co-GM                             | I can share campaign management responsibilities                      |
| GM     | Promote a player to co-GM                                | I can elevate a trusted player to help manage the campaign            |
| Player | View a campaign I've been invited to                     | I can see campaign info and my character sheet                        |
| GM     | Give an item to a player by adding it to their inventory | I can reward loot without requiring manual entry on the player's side |
| GM     | Attach notes to a campaign _(Phase 2)_                   | I can track what happened in previous sessions                        |
| GM     | Mark a note or resource as Secret _(Future)_             | Players cannot see it until I choose to reveal it                     |
| GM     | Organize notes and resources into folders _(Future)_     | I can separate secret GM content from player-facing content           |
| GM     | Reveal a secret note or resource to players _(Future)_   | I can progressively share information as the story unfolds            |
| Player | See only the notes and resources the GM has made visible _(Future)_ | I only discover what my character would know               |

## Functional Requirements

### Phase 1 — MVP (Offline)

**Campaign CRUD**
- Create a campaign with: name, description, game system (e.g. D&D 5e).
- Edit and delete a campaign.
- List all campaigns owned by the user.

**Attached Resources**
- Attach/detach character sheets (PCs and NPCs) from the campaign roster.
- View the list of attached characters directly from the campaign detail screen.


### Phase 2 — Session Notes

Notes are defined as standalone entities in [PRD-001](PRD-001-REFERENCE-DATA.md). In this phase, a note can be attached to a campaign, turning it into a session note.

- Attach/detach an existing note to a campaign.
- List all notes attached to the campaign, sorted by last modified date.
- Notes remain in the user's personal library even when detached from a campaign.

### Phase 3 — Online with Sync

**Sharing & Access Control**

Campaign roles and their permissions:

| Role           | How assigned                                                 | Access                                                                        |
|----------------|--------------------------------------------------------------|-------------------------------------------------------------------------------|
| **GM** (owner) | Creates the campaign                                         | Full access: edit campaign, notes, roster, secret folders, manage all members |
| **Co-GM**      | Invited directly as co-GM, or promoted from Player by the GM | Same access as GM, except cannot delete the campaign or remove the owner      |
| **Player**     | Invited by the GM or a co-GM                                 | Read-only access to visible content; can create and edit own PC sheet(s)      |

- Any user can create a campaign; the creator is automatically the **GM** (owner).
- The GM and co-GMs can invite users as Players or co-GMs via username or email.
- The GM can promote any Player to co-GM, or demote a co-GM back to Player.
- The GM can revoke any member's access at any time.
- Sync all campaign data (notes, roles) across devices in real time.

**Secret Folders (Future)**
- Notes and resources can be organized into folders with a visibility setting: **Secret** (GM and co-GMs only) or **Visible** (all campaign members).
- The GM can toggle the visibility of any folder or item at any time.
- Secret content is completely hidden from Players — they cannot see its existence.
- By default, newly created notes and resources are **Secret**.

**Item Assignment (Future)**
- The GM can assign an item from the Item catalog (or a custom item) directly to a Player's inventory.
- The Player receives a notification and the item appears in their character sheet's inventory.
- The GM can optionally add a note when assigning the item (e.g. "Found in the chest in room 4").

**Notifications (future consideration)**
- Notify invited players when they are added to a campaign.
- Notify the GM when a player accepts or declines an invitation.

## Non-Functional Requirements

- A campaign and all its notes must remain accessible offline after the initial sync.
- Deleting a campaign must not delete the attached character sheets (they remain in the user's personal library).
- Access control changes must propagate to other devices within a reasonable time (target: < 60 seconds).

## Out of Scope

- Real-time collaborative editing of notes (e.g. Google Docs-style concurrent editing).
- In-app map or encounter board tools.
- Campaign templates or pre-built adventure modules.

## Open Questions

- Should notes support rich text formatting (markdown, bold, lists) or plain text only?
- Should the campaign detail show a combined activity feed (notes + roster changes)?
- What happens to a shared campaign when the owning GM deletes their account?
