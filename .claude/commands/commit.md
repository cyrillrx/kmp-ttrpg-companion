---
allowed-tools: Bash(git add:*), Bash(git status:*), Bash(git commit:*), Bash(git diff:*), Bash(git log:*)
description: Stage and commit current changes following the project's Conventional Commits conventions
---

## Context

- Current git status: !`git status`
- Staged and unstaged diff: !`git diff HEAD`
- Current branch: !`git branch --show-current`
- Recent commits (for style reference): !`git log --oneline -10`

## Commit format

Follow **Conventional Commits**: `<type>(<scope>): <subject>`

**Types**: `feat`, `fix`, `ui`, `refactor`, `style`, `docs`, `test`, `chore`, `ci`, `build`, `perf`, `security`
- `feat` — new user-facing functionality
- `ui` — visible UI change that is not a new feature (removing an icon, adjusting layout, tweaking a component)
- `refactor` — internal restructuring with no visible change to the user

**Scope**: component affected — use short names already present in the git log (e.g. `spell`, `home`, `campaign`, `creature`, `magicalitem`, `character`, `agents`, `roadmap`, `prd`, `compose-app`, `spring-server`)

**Subject rules**:
- Imperative, present tense: "add" not "added"
- Lowercase first letter
- No trailing period

**Forbidden**: do NOT add `Co-Authored-By` or any AI co-author trailer.

## Your task

Stage all unstaged changes and create a single commit. Use only the git tools available — no other output.
