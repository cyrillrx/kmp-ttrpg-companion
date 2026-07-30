# Go Backend Conventions

> [!IMPORTANT]
> **Canonical source of truth** (shared, project-agnostic): [`conventions/go-conventions.md`](https://github.com/cyrillrx/coding-conventions/blob/main/conventions/go-conventions.md) — do not duplicate here.

They apply as-is to the `server-go/` service. Only the project-specific bindings below differ.

## Project-specific additions

- **Store name**: the canonical `Store` interface is realized here as `CompendiumStore`
  (`internal/store/store.go`), backed by a JSON implementation (`internal/store/json_store.go`).
- **CI workflow**: Go checks run in [`.github/workflows/ci-server-go.yml`](../../.github/workflows/ci-server-go.yml)
  — `go build ./...`, `go test ./...`, and `go vet ./...` must all pass.
