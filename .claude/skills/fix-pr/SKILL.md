---
name: fix-pr
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

### Step 3 — Understand and fix each comment

For each unresolved thread:
1. Read the file at `path` around the relevant `line`
2. Understand what the reviewer is asking
3. Apply the fix using Edit or Write
4. Note the thread `id` for Step 5

If a comment requires judgment (e.g. design decision, ambiguous intent), explain the issue to the user and ask how to proceed before making the change.

### Step 4 — Ask for git permission

**Do NOT run any git commit, push, or rebase commands without explicit user approval.**

Summarise the fixes made (files changed, what was fixed), then ask:
> "May I commit and push these changes? Branch: `<branch>`, suggested commit message: `<conventional-commit-message>`"

Wait for approval before proceeding.

### Step 5 — Commit, push, and resolve threads

Once the user approves:

1. Stage and commit the changes using the approved message.
2. Push to the current branch.
3. For each thread fixed, resolve it via GraphQL:

```bash
gh api graphql -f query='
mutation($threadId:ID!) {
  resolveReviewThread(input:{threadId:$threadId}) {
    thread { isResolved }
  }
}' -f threadId=<THREAD_ID>
```

### Step 6 — Re-run /review

Invoke the `/review` skill with the same PR number to verify the state of the PR after fixes:

```
/review <PR_NUMBER>
```
