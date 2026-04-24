FROM rust:1.80-slim AS builder
WORKDIR /app
COPY server-rust/Cargo.toml server-rust/Cargo.lock ./
RUN mkdir src && echo "fn main() {}" > src/main.rs
RUN cargo build --release
COPY server-rust/src ./src
RUN touch src/main.rs && cargo build --release

FROM debian:bookworm-slim
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=builder /app/target/release/server-rust ./
COPY data/compendium ./data/compendium
ENV DATA_DIR=/app/data/compendium
EXPOSE 3000
CMD ["./server-rust"]
