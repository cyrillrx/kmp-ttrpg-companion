package handler

import (
	"encoding/json"
	"net/http"

	"ttrpg-companion/server-go/internal/store"
)

func ListSpells(s store.CompendiumStore) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(s.GetSpells())
	}
}
