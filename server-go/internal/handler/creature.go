package handler

import (
	"encoding/json"
	"log"
	"net/http"

	"ttrpg-companion/server-go/internal/store"
)

func ListMonsters(s store.CompendiumStore) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		err := json.NewEncoder(w).Encode(s.GetMonsters())
		if err != nil {
			log.Printf("encode monsters: %v", err)
		}
	}
}
