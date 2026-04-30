# PRD-005 — Generators

> **Status**: Draft — Pending technical feasibility study | **Version**: 0.1 | **Last updated**: 2026-03-15

> ⚠️ **This feature is still uncertain.** The LLM-based generation approach (Phase 3 and beyond) requires a dedicated technical feasibility study before committing to an implementation.
> Phase 1 is the only part considered stable enough to plan concretely.

## Overview

The Generators module helps users quickly get inspiration and ready-to-use content during a session: NPCs, locations, plants, food, magic items, and more.

The approach evolves across phases:
- **Phase 1**: Curated gallery of pre-written entries — browse, pick, save.
- **Phase 3**: LLM-powered generation via API, contextually aware of the campaign via MCP.
- **Future exploration**: Local model running on a separate client, pushing results to the server as notes in the user's account or campaign.

Generated content is saved as **Notes** (see [PRD-004](PRD-004-NOTES.md)) and managed through the same list/search/CRUD pattern.

## Goals

- Help users improvise content instantly during a session.
- Provide immediately usable, believable results.
- Allow saving and editing generated content as notes for reuse.

## Example of Categories

| Category             | Fields                                                                              |
|----------------------|-------------------------------------------------------------------------------------|
| NPC                  | Name, age, race/ancestry, physical description, personality trait, short background |
| Place / Organization | Name, short description (tavern, guild, faction, ship…)                             |
| Plant                | Name, physical description, consumption method, effect                              |
| Food & Drink         | Name, description, optional effect                                                  |
| Magic Item           | Name, type, appearance, effect/property                                             |

---

## Phase 1 — Curated Gallery (Offline)

Pre-written entries are bundled in the app and browsed like a gallery. No procedural generation — content is hand-crafted and curated.

### User Stories

| As a… | I want to…                                                   | So that…                                        |
|-------|--------------------------------------------------------------|-------------------------------------------------|
| User  | Browse a gallery of pre-written NPCs, locations, items, etc. | I can quickly pick one that fits my scene       |
| User  | Filter the gallery by category                               | I can narrow down to what I need                |
| User  | Save an entry to my notes                                    | I can reuse it later or attach it to a campaign |
| User  | Edit a saved entry                                           | I can adapt it to my specific context           |

### Functional Requirements

- [ ] Display a browsable, searchable gallery of curated entries per category.
- [ ] Filter by category (NPC, place, plant, etc.).
- [ ] Save any entry as a Note (PRD-004) with one tap.
- [ ] Edit and delete saved entries.
- [ ] All content bundled locally — no network required.

---

## Phase 3 — LLM Generation via API (Online)

> ⚠️ Requires technical feasibility study.

LLM-based generation using an API. The app exposes campaign context (tone, setting, existing characters, etc.) via **MCP** so generated content is coherent with the ongoing campaign.

### User Stories

| As a… | I want to…                                                    | So that…                                              |
|-------|---------------------------------------------------------------|-------------------------------------------------------|
| User  | Generate an NPC or location with one tap                      | I get a fresh, contextually relevant result instantly |
| User  | Provide context (campaign, tone, setting) to guide generation | The result fits my world rather than feeling generic  |
| User  | Save the generated result as a note                           | I can reuse or attach it to my campaign               |

### Functional Requirements

- [ ] Call the LLM API with a structured prompt and campaign context via MCP.
- [ ] Return a structured result matching the category's field schema.
- [ ] Display the result in the same UI as Phase 1 gallery entries.
- [ ] Save result as a Note (PRD-004).
- [ ] Gracefully degrade to Phase 1 gallery when offline.

---

## Future Exploration — Local Model on External Client

> ⚠️ Highly uncertain. Requires dedicated feasibility study.

A local LLM (e.g. Ollama, llama.cpp) runs on a separate device or machine (not the companion app itself). It generates structured content and pushes the result to the backend server, which delivers it as a note to the user's account or directly into a campaign.

**Open questions for the feasibility study:**
- What protocol connects the local model to the backend (REST, WebSocket, MCP)?
- How is authentication and user identity handled between the local client and the server?
- What guarantees do we have on output format from a local model?
- What is the latency and UX impact of an async delivery model (note appears after a delay)?

---

## Non-Functional Requirements

- Phase 1 gallery must load and filter in under 300ms, fully offline.
- Phase 3 API calls must show a loading state; timeout gracefully after 10 seconds.
- Generated content must always be editable before saving.

## Out of Scope

- Dungeon / map layout generators.
- Quest or plot hook generators.
- On-device LLM inference within the companion app itself.

## Open Questions

- Should the Phase 1 gallery be expandable via downloadable content packs (Phase 2)?
- Should saved generated content be shareable between users?
