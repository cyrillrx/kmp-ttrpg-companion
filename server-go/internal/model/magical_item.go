package model

// MagicalItem is the API response model for a compendium magical item.
type MagicalItem struct {
	ID          string `json:"id"`
	Title       string `json:"title"`
	Description string `json:"description"`
	Type        string `json:"type"`
	Rarity      string `json:"rarity"`
	Attunement  bool   `json:"attunement"`
}

// MagicalItemJson mirrors the raw JSON structure of magical-items.json.
type MagicalItemJson struct {
	Title      *string `json:"title"`
	Content    *string `json:"content"`
	Type       *string `json:"type"`
	Rarity     *string `json:"rarity"`
	Attunement *string `json:"attunement"`
}
