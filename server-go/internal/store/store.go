package store

import "ttrpg-companion/server-go/internal/model"

// CompendiumStore is the data access interface for compendium resources.
// Implementations can load from JSON files, a database, or any other source.
type CompendiumStore interface {
	GetSpells() []model.Spell
	GetMonsters() []model.Monster
	GetMagicalItems() []model.MagicalItem
}
