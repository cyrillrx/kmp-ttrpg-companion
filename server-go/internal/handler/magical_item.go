package handler

import (
	"encoding/json"
	"log"
	"net/http"

	"ttrpg-companion/server-go/internal/store"
)

func ListMagicalItems(s store.CompendiumStore) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		err := json.NewEncoder(w).Encode(s.GetMagicalItems())
		if err != nil {
			log.Printf("encode magical items: %v", err)
		}
	}
}
