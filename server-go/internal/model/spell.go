package model

import "encoding/json"

// Spell is the API response model for a compendium spell.
type Spell struct {
	ID               string                     `json:"id"`
	Source           string                     `json:"source"`
	Level            int                        `json:"level"`
	School           string                     `json:"school"`
	Concentration    bool                       `json:"concentration"`
	Ritual           bool                       `json:"ritual"`
	Components       SpellComponents            `json:"components"`
	AvailableClasses []string                   `json:"availableClasses"`
	Translations     map[string]SpellTranslation `json:"translations"`
}

type SpellComponents struct {
	Verbal   bool `json:"verbal"`
	Somatic  bool `json:"somatic"`
	Material bool `json:"material"`
}

type SpellTranslation struct {
	Name                string  `json:"name"`
	CastingTime         string  `json:"castingTime"`
	Range               string  `json:"range"`
	Duration            string  `json:"duration"`
	MaterialDescription *string `json:"materialDescription"`
	Description         string  `json:"description"`
}

// SpellJson mirrors the raw JSON structure of spells.json.
type SpellJson struct {
	ID               *string                        `json:"id"`
	Source           *string                        `json:"source"`
	Level            *int                           `json:"level"`
	School           *string                        `json:"school"`
	Concentration    *bool                          `json:"concentration"`
	Ritual           *bool                          `json:"ritual"`
	Components       *SpellComponentsJson           `json:"components"`
	AvailableClasses []string                       `json:"availableClasses"`
	Translations     map[string]SpellTranslationJson `json:"translations"`
}

type SpellComponentsJson struct {
	Verbal   bool `json:"verbal"`
	Somatic  bool `json:"somatic"`
	Material bool `json:"material"`
}

type SpellTranslationJson struct {
	Name                *string `json:"name"`
	CastingTime         *string `json:"castingTime"`
	Range               *string `json:"range"`
	Duration            *string `json:"duration"`
	MaterialDescription *string `json:"materialDescription"`
	Description         *string `json:"description"`
}

// Challenge is kept as RawMessage to handle both numeric and string CR values.
type Challenge = json.RawMessage
