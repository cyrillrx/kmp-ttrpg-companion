# Git & Collaboration Conventions

This document outlines the collaboration guidelines, Git conventions, and branching strategy used across the project.
Adhering to these conventions helps maintain a clean, understandable, and traceable project history.

Refer to `../../AGENTS.md` for overall project guidelines.

## 1. Collaboration & Communication

- **Respectful Interaction**: All interactions should be respectful and constructive.
- **Clear Communication**: Be clear and concise in your communications (code comments, commit messages, pull request descriptions).
- **Ask Questions**: Don't hesitate to ask questions if something is unclear.

## 2. Conventional Commits

We follow **Conventional Commits** to keep the history clean and to allow automated changelog generation.

### Commit Format

```
<type>(<scope>): <subject>

<body>
```

- **Types**: `feat`, `fix`, `refactor`, `style`, `docs`, `test`, `chore`, `ci`, `build` (and potentially `perf`, `security`).
- **Scope**: The component affected (e.g., `project`, `compose-app`, `server-auth`, `bruno-api`, `agents`, `prd`).
- **Subject**: Use the imperative, present tense: "change" not "changed" nor "changes". Don't capitalize the first letter. No dot (.) at the end.

## 3. Commit Examples

Here are some examples of valid commit messages:

- feat(compose-app): add new campaign creation form
- fix(server-auth): prevent XSS attack in login endpoint
- docs(project): update README.md with new project overview
- docs(pdr): add product requirement document to describe spell filtering 
- docs(agents): add co-authorship rule to AGENTS.md
- style(compose-app): format `CampaignListScreen.kt` with ktlint
- refactor(spring-server): extract common logging logic to a utility class
- test(bruno-api): add E2E test for user registration flow
- build(compose-app): update gradle wrapper to 8.x

## 4. Trunk-Based Development Branching Strategy

We utilize a **Trunk-Based Development** branching strategy. This means:

- **Single Main Branch:** All development happens directly on or very close to a single main branch (often named `main`).
- **Small, Frequent Commits:** Developers commit small changes frequently to the main branch, often multiple times a day.
- **Short-Lived Feature Branches:** If feature branches are used, they are kept very short-lived and merged back into the main branch as quickly as possible (typically within a day or two). Branch names mirror commit types: `feat/short-description`, `fix/issue-description`, `refactor/short-description`, `docs/short-description`, `chore/short-description`, etc. (e.g., `feat/create-campaign`, `fix/login-xss`).
- **Continuous Integration:** A robust Continuous Integration (CI) system is essential to automatically build and test changes pushed to the main branch, catching integration issues early.
- **Feature Flags:** To deploy incomplete features without affecting users, feature flags (also known as feature toggles) are used to enable or disable features at runtime. This allows for continuous delivery and reduces the need for long-lived feature branches.

This approach promotes continuous integration, reduces merge conflicts, and enables faster delivery of features.

## 5. CI & Policies

- **Pull Requests**: All code must be reviewed via PRs before merging into `main`.
- **Code Quality**: PRs must pass all automated checks (linting, tests, build) on the CI pipeline.
- **Warnings as Errors**: Treat compiler warnings seriously. Fix them proactively.
- **Security Scans**: Integrate automated security scanning where applicable.
