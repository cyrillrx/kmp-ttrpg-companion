package store

import (
	"encoding/json"
	"fmt"
	"os"
	"path/filepath"

	"ttrpg-companion/server-go/internal/model"
)

// JsonCompendiumStore loads compendium data from JSON files at startup.
type JsonCompendiumStore struct {
	spells       []model.Spell
	monsters     []model.Monster
	magicalItems []model.MagicalItem
}

// NewJsonCompendiumStore loads all three compendium JSON files from dataDir.
func NewJsonCompendiumStore(dataDir string) (*JsonCompendiumStore, error) {
	spells, err := loadSpells(filepath.Join(dataDir, "spells.json"))
	if err != nil {
		return nil, fmt.Errorf("loading spells: %w", err)
	}

	monsters, err := loadMonsters(filepath.Join(dataDir, "monsters.json"))
	if err != nil {
		return nil, fmt.Errorf("loading monsters: %w", err)
	}

	items, err := loadMagicalItems(filepath.Join(dataDir, "magical-items.json"))
	if err != nil {
		return nil, fmt.Errorf("loading magical items: %w", err)
	}

	return &JsonCompendiumStore{
		spells:       spells,
		monsters:     monsters,
		magicalItems: items,
	}, nil
}

func (s *JsonCompendiumStore) GetSpells() []model.Spell             { return s.spells }
func (s *JsonCompendiumStore) GetMonsters() []model.Monster         { return s.monsters }
func (s *JsonCompendiumStore) GetMagicalItems() []model.MagicalItem { return s.magicalItems }

// ── Loaders ───────────────────────────────────────────────────────────────────

func loadSpells(path string) ([]model.Spell, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, err
	}

	var raw []model.SpellJson
	if err = json.Unmarshal(data, &raw); err != nil {
		return nil, err
	}

	spells := make([]model.Spell, 0, len(raw))
	for _, r := range raw {
		if s, ok := spellFromJson(r); ok {
			spells = append(spells, s)
		}
	}
	return spells, nil
}

func loadMonsters(path string) ([]model.Monster, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, err
	}

	var raw []model.MonsterJson
	if err = json.Unmarshal(data, &raw); err != nil {
		return nil, err
	}

	monsters := make([]model.Monster, 0, len(raw))
	for _, r := range raw {
		if m, ok := monsterFromJson(r); ok {
			monsters = append(monsters, m)
		}
	}
	return monsters, nil
}

func loadMagicalItems(path string) ([]model.MagicalItem, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, err
	}

	var raw []model.MagicalItemJson
	if err = json.Unmarshal(data, &raw); err != nil {
		return nil, err
	}

	items := make([]model.MagicalItem, 0, len(raw))
	for _, r := range raw {
		if item, ok := magicalItemFromJson(r); ok {
			items = append(items, item)
		}
	}
	return items, nil
}

// ── Converters ────────────────────────────────────────────────────────────────

func spellFromJson(spell model.SpellJson) (model.Spell, bool) {
	if spell.ID == nil || *spell.ID == "" {
		fmt.Println("WARNING: skipping spell with missing id")
		return model.Spell{}, false
	}
	id := *spell.ID
	if spell.Source == nil {
		fmt.Printf("WARNING: skipping spell '%s': missing source\n", id)
		return model.Spell{}, false
	}
	if spell.Level == nil {
		fmt.Printf("WARNING: skipping spell '%s': missing level\n", id)
		return model.Spell{}, false
	}
	if spell.School == nil {
		fmt.Printf("WARNING: skipping spell '%s': missing school\n", id)
		return model.Spell{}, false
	}
	if spell.Concentration == nil {
		fmt.Printf("WARNING: skipping spell '%s': missing concentration\n", id)
		return model.Spell{}, false
	}
	if spell.Ritual == nil {
		fmt.Printf("WARNING: skipping spell '%s': missing ritual\n", id)
		return model.Spell{}, false
	}
	if spell.Components == nil {
		fmt.Printf("WARNING: skipping spell '%s': missing components\n", id)
		return model.Spell{}, false
	}
	if spell.AvailableClasses == nil {
		fmt.Printf("WARNING: skipping spell '%s': missing availableClasses\n", id)
		return model.Spell{}, false
	}
	if len(spell.Translations) == 0 {
		fmt.Printf("WARNING: skipping spell '%s': missing translations\n", id)
		return model.Spell{}, false
	}

	translations := make(map[string]model.SpellTranslation, len(spell.Translations))
	for locale, t := range spell.Translations {
		translations[locale] = model.SpellTranslation{
			Name:                derefString(t.Name),
			CastingTime:         derefString(t.CastingTime),
			Range:               derefString(t.Range),
			Duration:            derefString(t.Duration),
			MaterialDescription: t.MaterialDescription,
			Description:         derefString(t.Description),
		}
	}

	return model.Spell{
		ID:            id,
		Source:        *spell.Source,
		Level:         *spell.Level,
		School:        *spell.School,
		Concentration: *spell.Concentration,
		Ritual:        *spell.Ritual,
		Components: model.SpellComponents{
			Verbal:   spell.Components.Verbal,
			Somatic:  spell.Components.Somatic,
			Material: spell.Components.Material,
		},
		AvailableClasses: spell.AvailableClasses,
		Translations:     translations,
	}, true
}

func monsterFromJson(r model.MonsterJson) (model.Monster, bool) {
	if r.ID == nil || *r.ID == "" {
		fmt.Println("WARNING: skipping monster with missing id")
		return model.Monster{}, false
	}
	id := *r.ID
	if r.Source == nil {
		fmt.Printf("WARNING: skipping monster '%s': missing source\n", id)
		return model.Monster{}, false
	}
	if r.Type == nil {
		fmt.Printf("WARNING: skipping monster '%s': missing type\n", id)
		return model.Monster{}, false
	}
	if r.Size == nil {
		fmt.Printf("WARNING: skipping monster '%s': missing size\n", id)
		return model.Monster{}, false
	}
	if r.Alignment == nil {
		fmt.Printf("WARNING: skipping monster '%s': missing alignment\n", id)
		return model.Monster{}, false
	}
	if r.Abilities == nil {
		fmt.Printf("WARNING: skipping monster '%s': missing abilities\n", id)
		return model.Monster{}, false
	}
	if len(r.Translations) == 0 {
		fmt.Printf("WARNING: skipping monster '%s': missing translations\n", id)
		return model.Monster{}, false
	}

	translations := make(map[string]model.Translation, len(r.Translations))
	for locale, t := range r.Translations {
		if t.Name == nil {
			fmt.Printf("WARNING: monster '%s' locale '%s': missing name\n", id, locale)
			continue
		}
		if t.Description == nil {
			fmt.Printf("WARNING: monster '%s' locale '%s': missing description\n", id, locale)
			continue
		}
		if t.Speed == nil {
			fmt.Printf("WARNING: monster '%s' locale '%s': missing speed\n", id, locale)
			continue
		}
		if t.Senses == nil {
			fmt.Printf("WARNING: monster '%s' locale '%s': missing senses\n", id, locale)
			continue
		}
		translations[locale] = model.Translation{
			Name:        *t.Name,
			Subtype:     t.Subtype,
			Description: *t.Description,
			Speed:       *t.Speed,
			Senses:      *t.Senses,
			Languages:   orEmptySlice(t.Languages),
		}
	}
	if len(translations) == 0 {
		fmt.Printf("WARNING: skipping monster '%s': no valid translations\n", id)
		return model.Monster{}, false
	}

	var cr float32
	if r.ChallengeRating != nil {
		cr = *r.ChallengeRating
	}
	ac := 10
	if r.ArmorClass != nil {
		ac = *r.ArmorClass
	}
	var hp int
	if r.MaxHitPoints != nil {
		hp = *r.MaxHitPoints
	}

	return model.Monster{
		ID:                  id,
		Source:              *r.Source,
		Type:                *r.Type,
		Size:                *r.Size,
		Alignment:           *r.Alignment,
		ChallengeRating:     cr,
		ArmorClass:          ac,
		MaxHitPoints:        hp,
		HitDice:             derefString(r.HitDice),
		Abilities:           abilityGroupFromJson(r.Abilities),
		Skills:              orEmptyStringMap(r.Skills),
		DamageAffinities:    orEmptyStringMap(r.DamageAffinities),
		ConditionImmunities: orEmptyBoolMap(r.ConditionImmunities),
		Translations:        translations,
	}, true
}

func abilityGroupFromJson(a *model.AbilitiesJson) model.Abilities {
	if a == nil {
		return model.Abilities{
			Str: model.Ability{Value: 10},
			Dex: model.Ability{Value: 10},
			Con: model.Ability{Value: 10},
			Int: model.Ability{Value: 10},
			Wis: model.Ability{Value: 10},
			Cha: model.Ability{Value: 10},
		}
	}
	return model.Abilities{
		Str: abilityFromJson(a.Str),
		Dex: abilityFromJson(a.Dex),
		Con: abilityFromJson(a.Con),
		Int: abilityFromJson(a.Int),
		Wis: abilityFromJson(a.Wis),
		Cha: abilityFromJson(a.Cha),
	}
}

func abilityFromJson(a *model.AbilityJson) model.Ability {
	if a == nil {
		return model.Ability{Value: 10}
	}
	v := 10
	if a.Value != nil {
		v = *a.Value
	}
	return model.Ability{Value: v, SavingThrowProficiency: a.SavingThrowProficiency}
}

func magicalItemFromJson(r model.MagicalItemJson) (model.MagicalItem, bool) {
	if r.ID == nil || *r.ID == "" {
		fmt.Println("WARNING: skipping magical item with missing id")
		return model.MagicalItem{}, false
	}
	id := *r.ID
	if r.Source == nil {
		fmt.Printf("WARNING: skipping magical item '%s': missing source\n", id)
		return model.MagicalItem{}, false
	}
	if r.Type == nil {
		fmt.Printf("WARNING: skipping magical item '%s': missing type\n", id)
		return model.MagicalItem{}, false
	}
	if r.Rarity == nil {
		fmt.Printf("WARNING: skipping magical item '%s': missing rarity\n", id)
		return model.MagicalItem{}, false
	}
	if len(r.Translations) == 0 {
		fmt.Printf("WARNING: skipping magical item '%s': missing translations\n", id)
		return model.MagicalItem{}, false
	}

	translations := make(map[string]model.MagicalItemTranslation, len(r.Translations))
	for locale, t := range r.Translations {
		if t.Name == nil {
			fmt.Printf("WARNING: magical item '%s' locale '%s': missing name\n", id, locale)
			continue
		}
		if t.Description == nil {
			fmt.Printf("WARNING: magical item '%s' locale '%s': missing description\n", id, locale)
			continue
		}
		translations[locale] = model.MagicalItemTranslation{
			Name:        *t.Name,
			Subtype:     t.Subtype,
			Description: *t.Description,
		}
	}
	if len(translations) == 0 {
		fmt.Printf("WARNING: skipping magical item '%s': no valid translations\n", id)
		return model.MagicalItem{}, false
	}

	attunement := r.Attunement != nil && *r.Attunement

	return model.MagicalItem{
		ID:           id,
		Source:       *r.Source,
		Type:         *r.Type,
		Rarity:       *r.Rarity,
		Attunement:   attunement,
		Translations: translations,
	}, true
}

// ── Nil-safe helpers ──────────────────────────────────────────────────────────

func derefString(s *string) string {
	if s == nil {
		return ""
	}
	return *s
}

func orEmptySlice(s []string) []string {
	if s == nil {
		return []string{}
	}
	return s
}

func orEmptyStringMap(m map[string]string) map[string]string {
	if m == nil {
		return map[string]string{}
	}
	return m
}

func orEmptyBoolMap(m map[string]bool) map[string]bool {
	if m == nil {
		return map[string]bool{}
	}
	return m
}
