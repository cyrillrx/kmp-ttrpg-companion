package store

import (
	"encoding/json"
	"fmt"
	"os"
	"path/filepath"
	"strconv"
	"strings"

	"ttrpg-companion/server-go/internal/model"
)

// JsonCompendiumStore loads compendium data from JSON files at startup.
type JsonCompendiumStore struct {
	spells       []model.Spell
	creatures    []model.Creature
	magicalItems []model.MagicalItem
}

// NewJsonCompendiumStore loads all three compendium JSON files from dataDir.
func NewJsonCompendiumStore(dataDir string) (*JsonCompendiumStore, error) {
	spells, err := loadSpells(filepath.Join(dataDir, "spells.json"))
	if err != nil {
		return nil, fmt.Errorf("loading spells: %w", err)
	}

	creatures, err := loadCreatures(filepath.Join(dataDir, "creatures.json"))
	if err != nil {
		return nil, fmt.Errorf("loading creatures: %w", err)
	}

	items, err := loadMagicalItems(filepath.Join(dataDir, "magical-items.json"))
	if err != nil {
		return nil, fmt.Errorf("loading magical items: %w", err)
	}

	return &JsonCompendiumStore{
		spells:       spells,
		creatures:    creatures,
		magicalItems: items,
	}, nil
}

func (s *JsonCompendiumStore) GetSpells() []model.Spell       { return s.spells }
func (s *JsonCompendiumStore) GetCreatures() []model.Creature { return s.creatures }
func (s *JsonCompendiumStore) GetMagicalItems() []model.MagicalItem { return s.magicalItems }

// ── Loaders ───────────────────────────────────────────────────────────────────

func loadSpells(path string) ([]model.Spell, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, err
	}

	var raw []model.SpellJson
	if err := json.Unmarshal(data, &raw); err != nil {
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

func loadCreatures(path string) ([]model.Creature, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, err
	}

	var raw []model.CreatureJson
	if err := json.Unmarshal(data, &raw); err != nil {
		return nil, err
	}

	creatures := make([]model.Creature, 0, len(raw))
	for _, r := range raw {
		if c, ok := creatureFromJson(r); ok {
			creatures = append(creatures, c)
		}
	}
	return creatures, nil
}

func loadMagicalItems(path string) ([]model.MagicalItem, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, err
	}

	var raw []model.MagicalItemJson
	if err := json.Unmarshal(data, &raw); err != nil {
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

func spellFromJson(r model.SpellJson) (model.Spell, bool) {
	if r.Title == nil || *r.Title == "" {
		return model.Spell{}, false
	}

	var schools, classes []string
	if r.Header != nil && r.Header.Taxonomy != nil {
		schools = r.Header.Taxonomy.SpellSchool
		classes = r.Header.Taxonomy.SpellClass
	}

	return model.Spell{
		ID:               *r.Title,
		Title:            *r.Title,
		Description:      derefString(r.Content),
		Level:            derefInt(r.Level),
		CastingTime:      derefString(r.CastingTime),
		Range:            derefString(r.Range),
		Components:       derefString(r.Components),
		Duration:         derefString(r.Duration),
		Schools:          orEmptySlice(schools),
		AvailableClasses: orEmptySlice(classes),
	}, true
}

func creatureFromJson(r model.CreatureJson) (model.Creature, bool) {
	if r.Title == nil || *r.Title == "" {
		return model.Creature{}, false
	}
	if r.Header == nil {
		return model.Creature{}, false
	}

	m := r.Header.Monster
	creatureType, subtype := parseTypeSubtype(derefString(r.TrueType))

	return model.Creature{
		ID:              *r.Title,
		Name:            *r.Title,
		Description:     derefString(r.Content),
		Type:            creatureType,
		Subtype:         subtype,
		Size:            derefString(r.Size),
		Alignment:       derefString(r.Alignment),
		ChallengeRating: parseChallenge(r.Challenge),
		ArmorClass:      parseAC(m),
		MaxHitPoints:    parseHP(m),
		Speed:           parseMonsterString(m, func(mn *model.MonsterJson) *string { return mn.Speed }),
		Languages:       parseLanguages(m),
		Abilities:       parseAbilities(m),
	}, true
}

func magicalItemFromJson(r model.MagicalItemJson) (model.MagicalItem, bool) {
	if r.Title == nil || *r.Title == "" {
		return model.MagicalItem{}, false
	}

	return model.MagicalItem{
		ID:          *r.Title,
		Title:       *r.Title,
		Description: derefString(r.Content),
		Type:        derefString(r.Type),
		Rarity:      derefString(r.Rarity),
		Attunement:  r.Attunement != nil && *r.Attunement != "",
	}, true
}

// ── Parsing helpers ───────────────────────────────────────────────────────────

// parseTypeSubtype splits "Humanoïde (Goblinoïde)" into ("Humanoïde", "Goblinoïde").
func parseTypeSubtype(s string) (string, string) {
	idx := strings.IndexByte(s, '(')
	if idx < 0 {
		return strings.TrimSpace(s), ""
	}
	t := strings.TrimSpace(s[:idx])
	sub := strings.TrimSpace(strings.TrimSuffix(strings.TrimSpace(s[idx+1:]), ")"))
	return t, sub
}

// parseChallenge handles both numeric and string challenge ratings (e.g. "0.5").
func parseChallenge(raw json.RawMessage) float32 {
	if len(raw) == 0 {
		return 0
	}
	var f float64
	if err := json.Unmarshal(raw, &f); err == nil {
		return float32(f)
	}
	var s string
	if err := json.Unmarshal(raw, &s); err == nil {
		if f, err := strconv.ParseFloat(s, 32); err == nil {
			return float32(f)
		}
	}
	return 0
}

// parseAC handles both integer and string AC values.
func parseAC(m *model.MonsterJson) int {
	if m == nil || len(m.AC) == 0 || string(m.AC) == "null" {
		return 10
	}
	var i int
	if err := json.Unmarshal(m.AC, &i); err == nil {
		return i
	}
	var s string
	if err := json.Unmarshal(m.AC, &s); err == nil {
		return parseFirstInt(s, 10)
	}
	return 10
}

func parseHP(m *model.MonsterJson) int {
	if m == nil || m.HP == nil {
		return 0
	}
	return parseFirstInt(*m.HP, 0)
}

func parseAbilities(m *model.MonsterJson) model.Abilities {
	if m == nil {
		return model.Abilities{Str: 10, Dex: 10, Con: 10, Int: 10, Wis: 10, Cha: 10}
	}
	return model.Abilities{
		Str: parseAbilityScore(m.Str),
		Dex: parseAbilityScore(m.Dex),
		Con: parseAbilityScore(m.Con),
		Int: parseAbilityScore(m.Int),
		Wis: parseAbilityScore(m.Wis),
		Cha: parseAbilityScore(m.Cha),
	}
}

func parseLanguages(m *model.MonsterJson) []string {
	if m == nil || m.Languages == nil || *m.Languages == "" {
		return []string{}
	}
	parts := strings.Split(*m.Languages, ",")
	langs := make([]string, 0, len(parts))
	for _, p := range parts {
		if t := strings.TrimSpace(p); t != "" {
			langs = append(langs, t)
		}
	}
	return langs
}

func parseMonsterString(m *model.MonsterJson, getter func(*model.MonsterJson) *string) string {
	if m == nil {
		return ""
	}
	return derefString(getter(m))
}

// parseAbilityScore parses "10 (+0)" → 10.
func parseAbilityScore(s *string) int {
	if s == nil {
		return 10
	}
	return parseFirstInt(*s, 10)
}

// parseFirstInt extracts the first whitespace-delimited integer from s.
func parseFirstInt(s string, fallback int) int {
	fields := strings.Fields(s)
	if len(fields) == 0 {
		return fallback
	}
	n, err := strconv.Atoi(fields[0])
	if err != nil {
		return fallback
	}
	return n
}

// ── Nil-safe helpers ──────────────────────────────────────────────────────────

func derefString(s *string) string {
	if s == nil {
		return ""
	}
	return *s
}

func derefInt(i *int) int {
	if i == nil {
		return 0
	}
	return *i
}

func orEmptySlice(s []string) []string {
	if s == nil {
		return []string{}
	}
	return s
}
