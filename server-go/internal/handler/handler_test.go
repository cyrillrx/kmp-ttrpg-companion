package handler_test

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"

	"ttrpg-companion/server-go/internal/handler"
	"ttrpg-companion/server-go/internal/model"
	"ttrpg-companion/server-go/internal/store"
)

type mockStore struct{}

func (m *mockStore) GetSpells() []model.Spell             { return []model.Spell{} }
func (m *mockStore) GetCreatures() []model.Creature       { return []model.Creature{} }
func (m *mockStore) GetMagicalItems() []model.MagicalItem { return []model.MagicalItem{} }

var _ store.CompendiumStore = (*mockStore)(nil)

func newTestRouter(s store.CompendiumStore) http.Handler {
	r := chi.NewRouter()
	r.Get("/health", handler.Health)
	r.Route("/compendium", func(r chi.Router) {
		r.Use(middleware.SetHeader("Content-Type", "application/json"))
		r.Get("/spells", handler.ListSpells(s))
		r.Get("/creatures", handler.ListCreatures(s))
		r.Get("/magical-items", handler.ListMagicalItems(s))
	})
	return r
}

func TestHealth_returns200(t *testing.T) {
	assertStatus(t, "/health", http.StatusOK)
}

func TestListSpells_returns200(t *testing.T) {
	assertStatus(t, "/compendium/spells", http.StatusOK)
}

func TestListCreatures_returns200(t *testing.T) {
	assertStatus(t, "/compendium/creatures", http.StatusOK)
}

func TestListMagicalItems_returns200(t *testing.T) {
	assertStatus(t, "/compendium/magical-items", http.StatusOK)
}

func TestUnknownRoute_returns404(t *testing.T) {
	assertStatus(t, "/unknown", http.StatusNotFound)
}

func assertStatus(t *testing.T, path string, want int) {
	t.Helper()
	req := httptest.NewRequest(http.MethodGet, path, nil)
	w := httptest.NewRecorder()
	newTestRouter(&mockStore{}).ServeHTTP(w, req)
	if w.Code != want {
		t.Errorf("GET %s: got %d, want %d", path, w.Code, want)
	}
}
