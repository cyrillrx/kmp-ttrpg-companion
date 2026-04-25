package main

import (
	"fmt"
	"log"
	"net/http"
	"os"
	"strings"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"

	"ttrpg-companion/server-go/internal/handler"
	"ttrpg-companion/server-go/internal/store"
)

func main() {
	dataDir := os.Getenv("DATA_DIR")
	if dataDir == "" {
		dataDir = "../data/compendium"
	}

	s, err := store.NewJsonCompendiumStore(dataDir)
	if err != nil {
		log.Fatalf("Failed to load compendium data from %q: %v", dataDir, err)
	}

	r := chi.NewRouter()
	r.Use(middleware.Logger)
	r.Use(buildCORSMiddleware())

	r.Get("/health", handler.Health)
	r.Route("/compendium", func(r chi.Router) {
		r.Use(middleware.SetHeader("Content-Type", "application/json"))
		r.Get("/spells", handler.ListSpells(s))
		r.Get("/creatures", handler.ListCreatures(s))
		r.Get("/magical-items", handler.ListMagicalItems(s))
	})

	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}
	addr := ":" + port
	fmt.Printf("Listening on %s\n", addr)
	if err := http.ListenAndServe(addr, r); err != nil {
		log.Fatal(err)
	}
}

func buildCORSMiddleware() func(http.Handler) http.Handler {
	allowedOrigins := os.Getenv("ALLOWED_ORIGINS")
	if allowedOrigins == "" {
		return cors.AllowAll().Handler
	}
	return cors.Handler(cors.Options{
		AllowedOrigins: strings.Split(allowedOrigins, ","),
		AllowedMethods: []string{"GET", "OPTIONS"},
		AllowedHeaders: []string{"Accept", "Content-Type"},
	})
}
