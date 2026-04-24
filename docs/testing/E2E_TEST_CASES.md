# E2E Test Cases

Functional test scenarios per feature, independent of tooling.
Automated flows are implemented with Maestro in `cmp-ttrpg-companion/.maestro/flows/`.

## User Lists

| ID    | Scenario                                     | Success criterion                                 | Automated                                          |
|-------|----------------------------------------------|---------------------------------------------------|----------------------------------------------------|
| UL-01 | Create a spell list                          | The list appears in the Spellbooks screen         | ✅ `UL-01_create-spell-list.yaml`                 |
| UL-02 | Rename a list from its detail screen         | The updated name is shown in the top bar          | ✅ `UL-02_rename-spell-list.yaml`                 |
| UL-03 | Swipe to delete a list, then undo            | The list is restored after tapping Undo           | ✅ `UL-03_swipe-to-delete-with-undo.yaml`         |
| UL-04 | Add a spell to a list, navigate back, verify | The spell appears in the list detail              | ✅ `UL-04_add-spell-to-list-and-verify.yaml`      |

## Spellbook Compendium

| ID    | Scenario             | Success criterion                              | Automated |
|-------|----------------------|------------------------------------------------|-----------|
| SP-01 | Browse spells        | Spell list is displayed                        | ❌        |
| SP-02 | Search for a spell   | Results filtered to matching spells            | ❌        |
| SP-03 | Open a spell detail  | Spell card shows name, school, description     | ❌        |

## Bestiary Compendium

| ID    | Scenario               | Success criterion                              | Automated |
|-------|------------------------|------------------------------------------------|-----------|
| BE-01 | Browse creatures       | Creature list is displayed                     | ❌        |
| BE-02 | Open a creature detail | Creature card shows name and stats             | ❌        |

## Magical Items Compendium

| ID    | Scenario                   | Success criterion                              | Automated |
|-------|----------------------------|------------------------------------------------|-----------|
| MI-01 | Browse magical items       | Item list is displayed                         | ❌        |
| MI-02 | Open a magical item detail | Item card shows name, rarity, description      | ❌        |

## Campaigns

| ID    | Scenario               | Success criterion                              | Automated |
|-------|------------------------|------------------------------------------------|-----------|
| CA-01 | Create a campaign      | Campaign appears in the list                   | ❌        |
| CA-02 | Open a campaign detail | Campaign details are shown                     | ❌        |

## Character Sheets

| ID    | Scenario                | Success criterion                              | Automated |
|-------|-------------------------|------------------------------------------------|-----------|
| CS-01 | Create a character      | Character appears in the list                  | ❌        |
| CS-02 | Open a character detail | Character details are shown                    | ❌        |
