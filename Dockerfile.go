FROM golang:1.23-bookworm AS builder
WORKDIR /app
COPY server-go/go.mod server-go/go.sum ./
RUN go mod download
COPY server-go/ ./
RUN go build -o server-go ./cmd/server/

FROM debian:bookworm-slim
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=builder /app/server-go ./
COPY data/compendium ./data/compendium
ENV DATA_DIR=/app/data/compendium
EXPOSE 8080
CMD ["./server-go"]
