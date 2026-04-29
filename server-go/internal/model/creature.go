package model

// Monster is the API response model for a compendium monster.
type Monster struct {
	ID                  string                 `json:"id"`
	Source              string                 `json:"source"`
	Type                string                 `json:"type"`
	Size                string                 `json:"size"`
	Alignment           string                 `json:"alignment"`
	ChallengeRating     float32                `json:"challengeRating"`
	ArmorClass          int                    `json:"armorClass"`
	MaxHitPoints        int                    `json:"maxHitPoints"`
	HitDice             string                 `json:"hitDice"`
	Abilities           Abilities              `json:"abilities"`
	Speeds              Speeds                 `json:"speeds"`
	Skills              map[string]string      `json:"skills"`
	DamageAffinities    map[string]string      `json:"damageAffinities"`
	ConditionImmunities map[string]bool        `json:"conditionImmunities"`
	Translations        map[string]Translation `json:"translations"`
}

// Speeds holds the different movement speeds of a monster (values in feet).
type Speeds struct {
	Walk   *int  `json:"walk"`
	Fly    *int  `json:"fly"`
	Swim   *int  `json:"swim"`
	Climb  *int  `json:"climb"`
	Burrow *int  `json:"burrow"`
	Hover  bool  `json:"hover"`
}

// Ability holds a single D&D ability score with an optional saving throw proficiency.
type Ability struct {
	Value                  int     `json:"value"`
	SavingThrowProficiency *string `json:"savingThrowProficiency"`
}

// Abilities holds the six D&D ability scores.
type Abilities struct {
	Str Ability `json:"str"`
	Dex Ability `json:"dex"`
	Con Ability `json:"con"`
	Int Ability `json:"int"`
	Wis Ability `json:"wis"`
	Cha Ability `json:"cha"`
}

// Translation holds locale-specific text for a monster.
type Translation struct {
	Name        string   `json:"name"`
	Subtype     *string  `json:"subtype"`
	Description string   `json:"description"`
	Senses      string   `json:"senses"`
	Languages   []string `json:"languages"`
}

// MonsterJson mirrors the raw JSON structure of monsters.json.
type MonsterJson struct {
	ID                  *string                    `json:"id"`
	Source              *string                    `json:"source"`
	Type                *string                    `json:"type"`
	Size                *string                    `json:"size"`
	Alignment           *string                    `json:"alignment"`
	ChallengeRating     *float32                   `json:"challengeRating"`
	ArmorClass          *int                       `json:"armorClass"`
	MaxHitPoints        *int                       `json:"maxHitPoints"`
	HitDice             *string                    `json:"hitDice"`
	Abilities           *AbilitiesJson             `json:"abilities"`
	Speeds              *SpeedsJson                `json:"speeds"`
	Skills              map[string]string          `json:"skills"`
	DamageAffinities    map[string]string          `json:"damageAffinities"`
	ConditionImmunities map[string]bool            `json:"conditionImmunities"`
	Translations        map[string]TranslationJson `json:"translations"`
}

type AbilitiesJson struct {
	Str *AbilityJson `json:"str"`
	Dex *AbilityJson `json:"dex"`
	Con *AbilityJson `json:"con"`
	Int *AbilityJson `json:"int"`
	Wis *AbilityJson `json:"wis"`
	Cha *AbilityJson `json:"cha"`
}

type AbilityJson struct {
	Value                  *int    `json:"value"`
	SavingThrowProficiency *string `json:"savingThrowProficiency"`
}

type SpeedsJson struct {
	Walk   *int  `json:"walk"`
	Fly    *int  `json:"fly"`
	Swim   *int  `json:"swim"`
	Climb  *int  `json:"climb"`
	Burrow *int  `json:"burrow"`
	Hover  *bool `json:"hover"`
}

type TranslationJson struct {
	Name        *string  `json:"name"`
	Subtype     *string  `json:"subtype"`
	Description *string  `json:"description"`
	Senses      *string  `json:"senses"`
	Languages   []string `json:"languages"`
}
