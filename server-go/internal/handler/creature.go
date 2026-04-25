package handler

import (
	"encoding/json"
	"log"
	"net/http"

	"ttrpg-companion/server-go/internal/store"
)

func ListCreatures(s store.CompendiumStore) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if err := json.NewEncoder(w).Encode(s.GetCreatures()); err != nil {
			log.Printf("encode creatures: %v", err)
		}
	}
}
