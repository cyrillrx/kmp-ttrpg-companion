package model

// MagicalItem is the API response model for a compendium magical item.
type MagicalItem struct {
	ID           string                            `json:"id"`
	Source       string                            `json:"source"`
	Type         string                            `json:"type"`
	Rarity       string                            `json:"rarity"`
	Attunement   bool                              `json:"attunement"`
	Translations map[string]MagicalItemTranslation `json:"translations"`
}

// MagicalItemTranslation holds locale-specific fields for a magical item.
type MagicalItemTranslation struct {
	Name        string  `json:"name"`
	Subtype     *string `json:"subtype"`
	Description string  `json:"description"`
}

// MagicalItemJson mirrors the raw JSON structure of magical-items.json.
type MagicalItemJson struct {
	ID           *string                                `json:"id"`
	Source       *string                                `json:"source"`
	Type         *string                                `json:"type"`
	Rarity       *string                                `json:"rarity"`
	Attunement   *bool                                  `json:"attunement"`
	Translations map[string]MagicalItemTranslationJson  `json:"translations"`
}

// MagicalItemTranslationJson mirrors the raw JSON translation entry.
type MagicalItemTranslationJson struct {
	Name        *string `json:"name"`
	Subtype     *string `json:"subtype"`
	Description *string `json:"description"`
}
