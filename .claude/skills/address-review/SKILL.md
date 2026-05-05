---
name: address-review
description: Address open review comments on a PR, reply to each thread, resolve approved ones, then re-run /review
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
              databaseId
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
- ✅ **Apply** — comment is valid, clear, consistent with project conventions, and backed by a justification or source
- ⚠️ **Discuss** — comment requires a design decision, is ambiguous, or contradicts existing conventions
- ❌ **Skip** — comment is factually wrong, out of scope, already addressed, or not backed by any source or justification

Per project conventions, reviewers are expected to back their requests with sources (docs, articles, benchmarks). A comment that expresses personal preference without justification should be marked ❌ or ⚠️.

Wait for the user to confirm, adjust, or override each recommendation before proceeding.

### Step 4 — Apply fixes and reply to all threads

For each thread, apply the outcome and post a reply via the GitHub REST API.

For **inline** review comments (attached to a file and line):

```bash
gh api repos/<OWNER>/<REPO>/pulls/comments/<COMMENT_DATABASE_ID>/replies \
  -X POST -f body="<reply>"
```

Use the `databaseId` of the **first** comment in the thread as `<COMMENT_DATABASE_ID>`.

For **general** PR comments (no associated file/line — the above endpoint returns 404):

```bash
gh api repos/<OWNER>/<REPO>/issues/<PR_NUMBER>/comments \
  -X POST -f body="<reply>"
```

**Reply tone per outcome:**

- ✅ **Apply**: briefly confirm what was changed (e.g. "Fixed — renamed `X` to `Y` in `path/to/file.kt`.")
- ⚠️ **Discuss**: ask for clarification or a source (e.g. "Could you point to a reference or clarify the intent? Happy to adjust once the approach is clearer.")
- ❌ **Skip**: explain concisely why the comment is being declined, citing conventions or sources where applicable (e.g. "Keeping the current approach — it follows the pattern established in `docs/conventions/X.md`. Feel free to reopen if you disagree.")

Do not touch code for ⚠️ or ❌ threads.

### Step 5 — Ask for git permission

**Do NOT run any git commit, push, or rebase commands without explicit user approval.**

Note: `git add`, `git commit`, and `git push` are intentionally absent from `allowed-tools` so that each git operation requires explicit user confirmation in the Claude Code UI — in addition to the approval requested below.

Summarise the fixes made (files changed, what was fixed), then ask:
> "May I commit and push these changes? Branch: `<branch>`, suggested commit message: `<conventional-commit-message>`"

Wait for approval before proceeding.

### Step 6 — Commit, push, and resolve threads

Once the user approves:

1. Stage and commit the changes using the approved message.
2. Push to the current branch.
3. For each ✅ thread only, resolve it via GraphQL:

```bash
gh api graphql -f query='
mutation($threadId:ID!) {
  resolveReviewThread(input:{threadId:$threadId}) {
    thread { isResolved }
  }
}' -f threadId=<THREAD_ID>
```

⚠️ and ❌ threads are left open for the reviewer to follow up.

### Step 7 — Re-run /review

Invoke the `/review` skill with the same PR number to verify the state of the PR after fixes:

```
/review <PR_NUMBER>
```
