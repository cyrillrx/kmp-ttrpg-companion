package handler

import (
	"encoding/json"
	"log"
	"net/http"

	"ttrpg-companion/server-go/internal/store"
)

func ListMagicalItems(s store.CompendiumStore) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if err := json.NewEncoder(w).Encode(s.GetMagicalItems()); err != nil {
			log.Printf("encode magical items: %v", err)
		}
	}
}
