package model

// Spell is the API response model for a compendium spell.
type Spell struct {
	ID              string   `json:"id"`
	Title           string   `json:"title"`
	Description     string   `json:"description"`
	Level           int      `json:"level"`
	CastingTime     string   `json:"castingTime"`
	Range           string   `json:"range"`
	Components      string   `json:"components"`
	Duration        string   `json:"duration"`
	Schools         []string `json:"schools"`
	AvailableClasses []string `json:"availableClasses"`
}

// SpellJson mirrors the raw JSON structure of spells.json.
type SpellJson struct {
	Title       *string         `json:"title"`
	Content     *string         `json:"content"`
	Level       *int            `json:"level"`
	CastingTime *string         `json:"casting_time"`
	Range       *string         `json:"range"`
	Components  *string         `json:"components"`
	Duration    *string         `json:"duration"`
	Header      *SpellHeaderJson `json:"header"`
}

type SpellHeaderJson struct {
	Taxonomy *SpellTaxonomy `json:"taxonomy"`
}

type SpellTaxonomy struct {
	SpellSchool []string `json:"spell_school"`
	SpellClass  []string `json:"spell_class"`
}
