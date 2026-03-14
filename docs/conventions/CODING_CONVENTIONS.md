# General Coding Conventions

This document outlines the general coding philosophy, Clean Code principles, and commenting guidelines for the entire project, regardless of the underlying technology stack (Kotlin, Rust, etc.). 

For language-specific rules, refer to the respective technology convention files. Refer to `../../AGENTS.md` for overall project guidelines.

## 1. The Golden Rule: Self-Documenting Code

The primary goal when writing code is readability and maintainability. Your code should be the ultimate source of truth and should be self-sufficient in explaining its intent.

- **Expressive Naming**: Use clear, descriptive, and intention-revealing names for variables, functions, classes, and modules. If a name requires a comment to explain what it represents, the name is not good enough.
- **Small, Focused Functions**: Functions should do one thing, do it well, and do it only. If a function is doing too much, extract it into smaller, well-named sub-functions. This naturally eliminates the need for "header" comments inside long functions.
- **Fail Fast**: Validate inputs and preconditions early. Use language-specific tools (like `require()`, `check()` in Kotlin, or early `return Err()` / assertions in Rust) to halt execution when things go wrong, rather than letting invalid state propagate.

## 2. Comments

Comments are a double-edged sword. While sometimes necessary, they often become outdated and lie, whereas code must always execute truthfully.

### Inline Comments: The "Why", Not the "What"
- **Code is the "What" and "How"**: If you feel the need to write a comment to explain *what* a block of code is doing, you have likely failed to make the code clear enough. Extract a variable, rename a function, or simplify the logic instead.
- **Comments are the "Why"**: Only use inline comments to explain business context, complex domain logic that cannot be simplified, external constraints, or "hacky" workarounds that are necessary due to third-party limitations. Explain *why* a particular approach was chosen when it's not obvious.

### API Documentation (KDoc, Javadoc, Rustdocs)
- **Public Contracts**: While inline comments should be sparse, formal API documentation is highly encouraged (and often required) for public interfaces, shared modules, and core domain logic.
- **Purpose**: Use API documentation (e.g., KDoc in Kotlin, `///` in Rust) to define the contract: what parameters are expected, what the function returns, what errors it might throw, and its general behavior. This aids consumers of your API so they don't have to read the implementation details to use it.

## 3. General Paradigms

- **Favor Immutability**: By default, design data structures and variables to be immutable. Mutability increases cognitive load and is the source of many subtle bugs, especially in concurrent environments.
- **Formatting**: Do not argue over code formatting. Rely entirely on automated formatters and linters (`ktlint` for Kotlin, `rustfmt` and `clippy` for Rust). If the CI pipeline passes the formatting check, the formatting is correct.