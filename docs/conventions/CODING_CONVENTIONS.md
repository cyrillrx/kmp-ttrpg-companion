# General Coding Conventions

This document outlines the general coding philosophy and Clean Code principles for the entire project.
These rules apply regardless of the underlying technology stack (Kotlin, Rust, etc.).

For language-specific rules, refer to the respective technology convention files. Refer to `../../AGENTS.md` for overall project guidelines.

## 1. Meaningful Names

- **Intention-Revealing**: The name of a variable, function, or class should answer all the big questions. It should tell you why it exists, what it does, and how it is used. If a name requires a comment, then the name does not reveal its intent.
- **Class Names**: Classes and objects should have noun or noun phrase names like `Campaign`, `CharacterProfile`, or `AccountParser`. Avoid words like `Manager`, `Processor`, `Data`, or `Info`.
- **Method Names**: Methods should have verb or verb phrase names like `postPayment`, `deletePage`, or `save`.
- **Pronounceable and Searchable**: Use names that can be read aloud easily and are long enough to be easily searchable across the codebase.
- **Avoid Disinformation**: Avoid leaving false clues that obscure the meaning of code. Do not use abbreviations or types in the name (e.g., avoid `accountList` if it's not actually a `List`; use `accounts`).

## 2. Functions

- **Small**: The first rule of functions is that they should be small. 
- **Do One Thing**: Functions should do one thing and do it well. If a function does multiple things, extract them into separate functions.
- **One Level of Abstraction per Function**: Ensure that the statements within a function are all at the same level of abstraction. Don't mix high-level concepts with low-level details.
- **Function Arguments**: The ideal number of arguments for a function is zero (niladic). Next comes one (monadic), followed closely by two (dyadic). Three arguments (triadic) should be avoided where possible. More than three requires very special justification.
- **Command Query Separation (CQS)**: Functions should either do something (command) or answer something (query), but not both.
- **Avoid Side Effects**: Your function promises to do one thing, but it also does other *hidden* things. This is a lie and leads to temporal couplings and subtle bugs.

## 3. Comments

Comments are often considered failures to express intent in code. Code is the only truth.

- **Explain Yourself in Code**: The best comment is the one you don't need to write. Prioritize refactoring code to be self-explanatory over writing a comment to explain bad code.
- **Good Comments**: 
    - Legal comments (copyright).
    - Explanation of intent (the *Why*, not the *What*).
    - Warning of consequences (e.g., "Don't run this unless...").
    - TODO comments (use sparingly and clean them up regularly).
    - Public API documentation (e.g., KDoc, Rustdoc) to define contracts.
- **Bad Comments**: 
    - Redundant comments (explaining what the code obviously does).
    - Misleading comments.
    - Commented-out code: **Never leave commented-out code.** Delete it. Git will remember it.
    - Journal comments (changelogs in file headers). Use Git instead.

## 4. Error Handling

- **Use Exceptions/Results over Error Codes**: Depending on the language, use Exceptions (Kotlin) or Result types (Rust) rather than returning error flags or codes that clutter the caller's logic.
- **Extract Try/Catch Blocks**: Error handling is "one thing". Functions that handle errors should do nothing else. Extract the body of the `try` block (or the `match` block) into its own function.
- **Provide Context with Exceptions**: Create informative error messages and pass them along with your exceptions/results. Mention the operation that failed and the type of failure.
- **Fail Fast**: Validate preconditions early. Halt execution or return an error as soon as an invalid state is detected.

## 6. Code Organization and Formatting

- **The Newspaper Metaphor**: A source file should read like a newspaper article. The name should be simple but explanatory. The topmost parts should provide high-level concepts and algorithms. Detail should increase as we move downward.
- **Vertical Density and Distance**: Concepts that are closely related should be kept vertically close to each other. Local variables should be declared as close to their usage as possible.
- **Automated Formatting**: Do not argue over formatting rules. Rely entirely on automated formatters (`ktlint` for Kotlin, `rustfmt` for Rust). If the CI pipeline passes the formatting check, the formatting is correct.

## 7. Objects and Data Structures

- **Data Abstraction**: Hide the internal structure of your objects. Expose abstract interfaces that allow users to manipulate the *essence* of the data, without having to know its implementation.
- **Law of Demeter**: A module should not know about the innards of the objects it manipulates. Avoid "train wrecks" like `object.getA().getB().doSomething()`.
- **Favor Immutability**: By default, design data structures and variables to be immutable. It prevents side effects and makes concurrent programming significantly safer.
