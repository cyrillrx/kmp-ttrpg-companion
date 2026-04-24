package store

import (
	"encoding/json"
	"testing"

	"ttrpg-companion/server-go/internal/model"
)

// ── parseTypeSubtype ──────────────────────────────────────────────────────────

func TestParseTypeSubtype_withSubtype(t *testing.T) {
	typ, sub := parseTypeSubtype("Humanoïde (Goblinoïde)")
	if typ != "Humanoïde" || sub != "Goblinoïde" {
		t.Errorf("got (%q, %q)", typ, sub)
	}
}

func TestParseTypeSubtype_withoutSubtype(t *testing.T) {
	typ, sub := parseTypeSubtype("Bête")
	if typ != "Bête" || sub != "" {
		t.Errorf("got (%q, %q)", typ, sub)
	}
}

func TestParseTypeSubtype_empty(t *testing.T) {
	typ, sub := parseTypeSubtype("")
	if typ != "" || sub != "" {
		t.Errorf("got (%q, %q)", typ, sub)
	}
}

// ── parseChallenge ────────────────────────────────────────────────────────────

func TestParseChallenge_integer(t *testing.T) {
	raw, _ := json.Marshal(5)
	if got := parseChallenge(raw); got != 5 {
		t.Errorf("got %v", got)
	}
}

func TestParseChallenge_float(t *testing.T) {
	raw, _ := json.Marshal(0.5)
	if got := parseChallenge(raw); got != 0.5 {
		t.Errorf("got %v", got)
	}
}

func TestParseChallenge_stringFraction(t *testing.T) {
	raw, _ := json.Marshal("1/4")
	// "1/4" is not a valid float — expect fallback 0
	if got := parseChallenge(raw); got != 0 {
		t.Errorf("got %v, want 0", got)
	}
}

func TestParseChallenge_stringFloat(t *testing.T) {
	raw, _ := json.Marshal("0.5")
	if got := parseChallenge(raw); got != 0.5 {
		t.Errorf("got %v", got)
	}
}

func TestParseChallenge_empty(t *testing.T) {
	if got := parseChallenge(nil); got != 0 {
		t.Errorf("got %v", got)
	}
}

// ── parseFirstInt ─────────────────────────────────────────────────────────────

func TestParseFirstInt_plain(t *testing.T) {
	if got := parseFirstInt("15", 0); got != 15 {
		t.Errorf("got %v", got)
	}
}

func TestParseFirstInt_withModifier(t *testing.T) {
	if got := parseFirstInt("18 (+4)", 0); got != 18 {
		t.Errorf("got %v", got)
	}
}

func TestParseFirstInt_invalid(t *testing.T) {
	if got := parseFirstInt("abc", 42); got != 42 {
		t.Errorf("got %v", got)
	}
}

func TestParseFirstInt_empty(t *testing.T) {
	if got := parseFirstInt("", 7); got != 7 {
		t.Errorf("got %v", got)
	}
}

// ── parseLanguages ────────────────────────────────────────────────────────────

func TestParseLanguages_commaSeparated(t *testing.T) {
	s := "Commun, Elfique, Nain"
	m := &model.MonsterJson{Languages: &s}
	langs := parseLanguages(m)
	if len(langs) != 3 || langs[0] != "Commun" || langs[2] != "Nain" {
		t.Errorf("got %v", langs)
	}
}

func TestParseLanguages_nil(t *testing.T) {
	if langs := parseLanguages(nil); len(langs) != 0 {
		t.Errorf("got %v", langs)
	}
}

// ── spellFromJson ─────────────────────────────────────────────────────────────

func TestSpellFromJson_valid(t *testing.T) {
	title := "Boule de feu"
	lvl := 3
	r := model.SpellJson{Title: &title, Level: &lvl}
	s, ok := spellFromJson(r)
	if !ok {
		t.Fatal("expected ok")
	}
	if s.Title != "Boule de feu" || s.Level != 3 {
		t.Errorf("got %+v", s)
	}
}

func TestSpellFromJson_nilTitle(t *testing.T) {
	_, ok := spellFromJson(model.SpellJson{})
	if ok {
		t.Fatal("expected !ok for nil title")
	}
}

// ── magicalItemFromJson ───────────────────────────────────────────────────────

func TestMagicalItemFromJson_attunementPresent(t *testing.T) {
	title := "Anneau de protection"
	att := "Oui"
	r := model.MagicalItemJson{Title: &title, Attunement: &att}
	item, ok := magicalItemFromJson(r)
	if !ok || !item.Attunement {
		t.Errorf("expected attunement=true, got %+v", item)
	}
}

func TestMagicalItemFromJson_attunementEmpty(t *testing.T) {
	title := "Épée longue +1"
	empty := ""
	r := model.MagicalItemJson{Title: &title, Attunement: &empty}
	item, ok := magicalItemFromJson(r)
	if !ok || item.Attunement {
		t.Errorf("expected attunement=false, got %+v", item)
	}
}
