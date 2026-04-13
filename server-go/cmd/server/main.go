package main

import (
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"

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

	r.Get("/compendium/spells", handler.ListSpells(s))
	r.Get("/compendium/creatures", handler.ListCreatures(s))
	r.Get("/compendium/magical-items", handler.ListMagicalItems(s))

	addr := ":8080"
	fmt.Printf("Listening on %s\n", addr)
	if err := http.ListenAndServe(addr, r); err != nil {
		log.Fatal(err)
	}
}
