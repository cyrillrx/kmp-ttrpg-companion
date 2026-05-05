---
name: address-review
description: Address open review comments on a PR, resolve threads, then re-run /review
argument-hint: <pr-number>
allowed-tools:
  - Bash(gh api:*)
  - Bash(gh pr:*)
  - Bash(git status:*)
  - Bash(git diff:*)
  - Bash(git log:*)
  - Bash(git branch:*)
  - Read
  - Edit
  - Write
---

## Context

- PR number: $ARGUMENTS
- Current branch: !`git branch --show-current`

## Your task

Follow these steps in order.

### Step 1 — Identify the PR

If `$ARGUMENTS` is empty, run `gh pr list` and ask the user which PR to fix.
Otherwise use `$ARGUMENTS` as the PR number.

### Step 2 — Fetch open review threads

Use the GitHub GraphQL API to get all unresolved review threads with their comments:

```bash
gh api graphql -f query='
query($owner:String!, $repo:String!, $pr:Int!) {
  repository(owner:$owner, name:$repo) {
    pullRequest(number:$pr) {
      reviewThreads(first:100) {
        nodes {
          id
          isResolved
          comments(first:10) {
            nodes {
              body
              path
              line
              originalLine
            }
          }
        }
      }
    }
  }
}' -f owner=<OWNER> -f repo=<REPO> -F pr=<PR_NUMBER>
```

Determine `<OWNER>` and `<REPO>` dynamically before running the query:

```bash
gh repo view --json owner,name -q '"\(.owner.login) \(.name)"'
```

Filter for `isResolved: false` threads. If there are none, inform the user and stop.

### Step 3 — Triage comments

For each unresolved thread, read the file at `path` around the relevant `line`, then present a triage table to the user:

| # | File | Comment summary | Recommendation |
|---|------|-----------------|----------------|
| 1 | `path:line` | one-line summary | ✅ Apply / ⚠️ Discuss / ❌ Skip |
| … | | | |

**Recommendation criteria:**
- ✅ **Apply** — comment is valid, clear, and consistent with project conventions
- ⚠️ **Discuss** — comment requires a design decision, is ambiguous, or contradicts existing conventions
- ❌ **Skip** — comment is factually wrong, out of scope, or already addressed

Wait for the user to confirm, adjust, or override each recommendation before proceeding.

### Step 4 — Apply approved fixes

For each comment the user approved (✅):
1. Apply the fix using Edit or Write
2. Note the thread `id` for Step 7

Do not touch comments marked ⚠️ until the user has clarified intent. Do not touch comments marked ❌.

### Step 5 — Ask for git permission

**Do NOT run any git commit, push, or rebase commands without explicit user approval.**

Summarise the fixes made (files changed, what was fixed), then ask:
> "May I commit and push these changes? Branch: `<branch>`, suggested commit message: `<conventional-commit-message>`"

Wait for approval before proceeding.

### Step 6 — Commit, push, and resolve threads

Once the user approves:

1. Stage and commit the changes using the approved message.
2. Push to the current branch.
3. For each thread fixed (✅ only), resolve it via GraphQL:

```bash
gh api graphql -f query='
mutation($threadId:ID!) {
  resolveReviewThread(input:{threadId:$threadId}) {
    thread { isResolved }
  }
}' -f threadId=<THREAD_ID>
```

### Step 7 — Re-run /review

Invoke the `/review` skill with the same PR number to verify the state of the PR after fixes:

```
/review <PR_NUMBER>
```
