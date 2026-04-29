package store

import (
	"testing"

	"ttrpg-companion/server-go/internal/model"
)

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

// ── monsterFromJson ───────────────────────────────────────────────────────────

func makeMonsterJson() model.MonsterJson {
	id := "goblin"
	source := "mm2024"
	monsterType := "humanoid"
	size := "small"
	alignment := "neutral_evil"
	cr := float32(0.25)
	ac := 15
	hp := 7
	hitDice := "2d6"
	str := 8
	dex := 14
	con := 10
	intel := 10
	wis := 8
	cha := 8
	name := "Goblin"
	subtype := "Goblinoid"
	desc := "A small creature."
	speed := "30 ft."
	senses := "Darkvision 60 ft."
	return model.MonsterJson{
		ID:              &id,
		Source:          &source,
		Type:            &monsterType,
		Size:            &size,
		Alignment:       &alignment,
		ChallengeRating: &cr,
		ArmorClass:      &ac,
		MaxHitPoints:    &hp,
		HitDice:         &hitDice,
		Abilities: &model.AbilitiesJson{
			Str: &model.AbilityJson{Value: &str},
			Dex: &model.AbilityJson{Value: &dex},
			Con: &model.AbilityJson{Value: &con},
			Int: &model.AbilityJson{Value: &intel},
			Wis: &model.AbilityJson{Value: &wis},
			Cha: &model.AbilityJson{Value: &cha},
		},
		Translations: map[string]model.TranslationJson{
			"en": {
				Name:        &name,
				Subtype:     &subtype,
				Description: &desc,
				Speed:       &speed,
				Senses:      &senses,
				Languages:   []string{"Common", "Goblin"},
			},
		},
	}
}

func TestMonsterFromJson_valid(t *testing.T) {
	m, ok := monsterFromJson(makeMonsterJson())
	if !ok {
		t.Fatal("expected ok")
	}
	if m.ID != "goblin" || m.Type != "humanoid" || m.Size != "small" {
		t.Errorf("unexpected fields: %+v", m)
	}
	if m.Abilities.Str.Value != 8 || m.Abilities.Dex.Value != 14 {
		t.Errorf("unexpected abilities: %+v", m.Abilities)
	}
	en, hasEn := m.Translations["en"]
	if !hasEn {
		t.Fatal("expected 'en' translation")
	}
	if en.Name != "Goblin" {
		t.Errorf("got name %q", en.Name)
	}
	if en.Subtype == nil || *en.Subtype != "Goblinoid" {
		t.Errorf("got subtype %v", en.Subtype)
	}
	if len(en.Languages) != 2 {
		t.Errorf("got languages %v", en.Languages)
	}
}

func TestMonsterFromJson_missingId(t *testing.T) {
	_, ok := monsterFromJson(model.MonsterJson{})
	if ok {
		t.Fatal("expected !ok for missing id")
	}
}

func TestAbilityFromJson_defaultsTo10WhenNil(t *testing.T) {
	a := abilityFromJson(nil)
	if a.Value != 10 || a.SavingThrowProficiency != nil {
		t.Errorf("got %+v", a)
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
