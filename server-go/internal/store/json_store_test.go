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
	id := "fireball"
	source := "srd_5.1"
	lvl := 3
	school := "evocation"
	conc := false
	ritual := false
	name := "Fireball"
	castingTime := "1 action"
	rnge := "150 feet"
	duration := "Instantaneous"
	desc := "A bright streak flashes from your pointing finger."

	spell := model.SpellJson{
		ID:            &id,
		Source:        &source,
		Level:         &lvl,
		School:        &school,
		Concentration: &conc,
		Ritual:        &ritual,
		Components:    &model.SpellComponentsJson{Verbal: true, Somatic: true, Material: true},
		AvailableClasses: []string{"sorcerer", "wizard"},
		Translations: map[string]model.SpellTranslationJson{
			"en": {
				Name:        &name,
				CastingTime: &castingTime,
				Range:       &rnge,
				Duration:    &duration,
				Description: &desc,
			},
		},
	}
	s, ok := spellFromJson(spell)
	if !ok {
		t.Fatal("expected ok")
	}
	if s.ID != "fireball" || s.Level != 3 || s.School != "evocation" {
		t.Errorf("got %+v", s)
	}
	if _, hasEn := s.Translations["en"]; !hasEn {
		t.Error("expected 'en' translation")
	}
}

func TestSpellFromJson_nilId(t *testing.T) {
	_, ok := spellFromJson(model.SpellJson{})
	if ok {
		t.Fatal("expected !ok for nil id")
	}
}

// ── magicalItemFromJson ───────────────────────────────────────────────────────

func makeMagicalItemJson(id, source, itemType, rarity string, attunement bool, translations map[string]model.MagicalItemTranslationJson) model.MagicalItemJson {
	att := attunement
	return model.MagicalItemJson{
		ID:           &id,
		Source:       &source,
		Type:         &itemType,
		Rarity:       &rarity,
		Attunement:   &att,
		Translations: translations,
	}
}

func TestMagicalItemFromJson_valid(t *testing.T) {
	name := "Ring of Protection"
	desc := "<p>...</p>"
	r := makeMagicalItemJson("ring-of-protection", "srd_5.1", "ring", "rare", true, map[string]model.MagicalItemTranslationJson{
		"en": {Name: &name, Description: &desc},
	})
	item, ok := magicalItemFromJson(r)
	if !ok {
		t.Fatal("expected ok")
	}
	if item.ID != "ring-of-protection" || item.Type != "ring" || item.Rarity != "rare" || !item.Attunement {
		t.Errorf("got %+v", item)
	}
	if _, hasEn := item.Translations["en"]; !hasEn {
		t.Error("expected 'en' translation")
	}
}

func TestMagicalItemFromJson_attunementFalse(t *testing.T) {
	name := "Long Sword +1"
	desc := "<p>...</p>"
	r := makeMagicalItemJson("long-sword-plus-1", "srd_5.1", "weapon", "uncommon", false, map[string]model.MagicalItemTranslationJson{
		"en": {Name: &name, Description: &desc},
	})
	item, ok := magicalItemFromJson(r)
	if !ok || item.Attunement {
		t.Errorf("expected attunement=false, got %+v", item)
	}
}

func TestMagicalItemFromJson_missingId(t *testing.T) {
	_, ok := magicalItemFromJson(model.MagicalItemJson{})
	if ok {
		t.Fatal("expected !ok for missing id")
	}
}

// ── parseAC ───────────────────────────────────────────────────────────────────

func TestParseAC_fromNumber(t *testing.T) {
	m := &model.MonsterJson{AC: json.RawMessage(`15`)}
	if got := parseAC(m); got != 15 {
		t.Errorf("got %v", got)
	}
}

func TestParseAC_fromStringWithNote(t *testing.T) {
	m := &model.MonsterJson{AC: json.RawMessage(`"13 (armure naturelle)"`)}
	if got := parseAC(m); got != 13 {
		t.Errorf("got %v", got)
	}
}

func TestParseAC_nullDefaultsTo10(t *testing.T) {
	m := &model.MonsterJson{AC: json.RawMessage(`null`)}
	if got := parseAC(m); got != 10 {
		t.Errorf("got %v", got)
	}
}

// ── parseHP ───────────────────────────────────────────────────────────────────

func TestParseHP_extractsFirstNumber(t *testing.T) {
	hp := "52 (8d8 + 16)"
	m := &model.MonsterJson{HP: &hp}
	if got := parseHP(m); got != 52 {
		t.Errorf("got %v", got)
	}
}

func TestParseHP_emptyDefaultsTo0(t *testing.T) {
	empty := ""
	m := &model.MonsterJson{HP: &empty}
	if got := parseHP(m); got != 0 {
		t.Errorf("got %v", got)
	}
}

// ── parseAbilityScore ─────────────────────────────────────────────────────────

func TestParseAbilityScore_withModifier(t *testing.T) {
	s16 := "16 (+3)"
	if got := parseAbilityScore(&s16); got != 16 {
		t.Errorf("got %v", got)
	}
	s8 := "8 (-1)"
	if got := parseAbilityScore(&s8); got != 8 {
		t.Errorf("got %v", got)
	}
}

func TestParseAbilityScore_emptyDefaultsTo10(t *testing.T) {
	empty := ""
	if got := parseAbilityScore(&empty); got != 10 {
		t.Errorf("got %v", got)
	}
}

// ── parseLanguages (empty string) ─────────────────────────────────────────────

func TestParseLanguages_emptyStringReturnsEmpty(t *testing.T) {
	empty := ""
	m := &model.MonsterJson{Languages: &empty}
	if langs := parseLanguages(m); len(langs) != 0 {
		t.Errorf("got %v", langs)
	}
}
