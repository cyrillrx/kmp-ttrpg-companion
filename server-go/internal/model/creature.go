package model

import "encoding/json"

// Creature is the API response model for a compendium creature.
type Creature struct {
	ID              string    `json:"id"`
	Name            string    `json:"name"`
	Description     string    `json:"description"`
	Type            string    `json:"type"`
	Subtype         string    `json:"subtype"`
	Size            string    `json:"size"`
	Alignment       string    `json:"alignment"`
	ChallengeRating float32   `json:"challengeRating"`
	ArmorClass      int       `json:"armorClass"`
	MaxHitPoints    int       `json:"maxHitPoints"`
	Speed           string    `json:"speed"`
	Languages       []string  `json:"languages"`
	Abilities       Abilities `json:"abilities"`
}

// Abilities holds the six D&D ability scores.
type Abilities struct {
	Str int `json:"str"`
	Dex int `json:"dex"`
	Con int `json:"con"`
	Int int `json:"int"`
	Wis int `json:"wis"`
	Cha int `json:"cha"`
}

// CreatureJson mirrors the raw JSON structure of creatures.json.
type CreatureJson struct {
	Title     *string            `json:"title"`
	Content   *string            `json:"content"`
	TrueType  *string            `json:"truetype"`
	Size      *string            `json:"size"`
	Alignment *string            `json:"alignment"`
	Challenge json.RawMessage    `json:"challenge"`
	Header    *CreatureHeaderJson `json:"header"`
}

type CreatureHeaderJson struct {
	Monster *MonsterJson `json:"monster"`
}

type MonsterJson struct {
	AC        json.RawMessage `json:"ac"`
	HP        *string         `json:"hp"`
	Speed     *string         `json:"speed"`
	Str       *string         `json:"str"`
	Dex       *string         `json:"dex"`
	Con       *string         `json:"con"`
	Int       *string         `json:"int"`
	Wis       *string         `json:"wis"`
	Cha       *string         `json:"cha"`
	Languages *string         `json:"languages"`
}
