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

**Types**: `feat`, `fix`, `refactor`, `style`, `docs`, `test`, `chore`, `ci`, `build`, `perf`, `security`

**Scope**: component affected — use short names already present in the git log (e.g. `spell`, `home`, `campaign`, `creature`, `magicalitem`, `character`, `agents`, `roadmap`, `prd`, `compose-app`, `spring-server`)

**Subject rules**:
- Imperative, present tense: "add" not "added"
- Lowercase first letter
- No trailing period

**Forbidden**: do NOT add `Co-Authored-By` or any AI co-author trailer.

## Your task

Stage all unstaged changes and create a single commit. Use only the git tools available — no other output.
