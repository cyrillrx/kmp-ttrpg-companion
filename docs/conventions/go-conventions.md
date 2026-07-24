# Go Backend Conventions

> **Single source of truth — do not duplicate here.**
> The Go conventions (tech stack, layered architecture, coding style, testing) are maintained,
> project-agnostic, in the shared
> [`cyrillrx/coding-conventions`](https://github.com/cyrillrx/coding-conventions) repository.
>
> 📖 **Read the canonical document:**
> <https://github.com/cyrillrx/coding-conventions/blob/main/conventions/go-conventions.md>

They apply as-is to the `server-go/` service. Only the project-specific bindings below differ.

## Project-specific additions

- **Store name**: the canonical `Store` interface is realized here as `CompendiumStore`
  (`internal/store/store.go`), backed by a JSON implementation (`internal/store/json_store.go`).
- **CI workflow**: Go checks run in [`.github/workflows/ci-server-go.yml`](../../.github/workflows/ci-server-go.yml)
  — `go build ./...`, `go test ./...`, and `go vet ./...` must all pass.
