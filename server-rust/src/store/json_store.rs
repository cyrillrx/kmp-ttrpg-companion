use std::collections::HashMap;
use std::path::Path;

use serde::Deserialize;

use crate::models::{
    monster::{Abilities, Ability, Monster, Speeds, Translation},
    magical_item::{MagicalItem, MagicalItemTranslation},
    spell::{Spell, SpellComponents, SpellTranslation},
};
use crate::store::CompendiumStore;

pub struct JsonCompendiumStore {
    spells: Vec<Spell>,
    monsters: Vec<Monster>,
    magical_items: Vec<MagicalItem>,
}

impl JsonCompendiumStore {
    pub fn load(data_dir: &str) -> Result<Self, Box<dyn std::error::Error>> {
        let base = Path::new(data_dir);

        let spells_json = std::fs::read_to_string(base.join("spells.json"))?;
        let monsters_json = std::fs::read_to_string(base.join("monsters.json"))?;
        let items_json = std::fs::read_to_string(base.join("magical-items.json"))?;

        let spells = parse_spells(&spells_json)?;
        let monsters = parse_monsters(&monsters_json)?;
        let magical_items = parse_magical_items(&items_json)?;

        Ok(Self { spells, monsters, magical_items })
    }
}

impl CompendiumStore for JsonCompendiumStore {
    fn get_spells(&self) -> &[Spell] {
        &self.spells
    }

    fn get_monsters(&self) -> &[Monster] {
        &self.monsters
    }

    fn get_magical_items(&self) -> &[MagicalItem] {
        &self.magical_items
    }
}

// ── Raw JSON structs ──────────────────────────────────────────────────────────

#[derive(Deserialize)]
#[serde(rename_all = "camelCase")]
struct SpellJson {
    id: Option<String>,
    source: Option<String>,
    level: Option<i32>,
    school: Option<String>,
    concentration: Option<bool>,
    ritual: Option<bool>,
    components: Option<SpellComponentsJson>,
    available_classes: Option<Vec<String>>,
    translations: Option<HashMap<String, SpellTranslation>>,
}

#[derive(Deserialize)]
struct SpellComponentsJson {
    verbal: bool,
    somatic: bool,
    material: bool,
}

#[derive(Deserialize)]
#[serde(rename_all = "camelCase")]
struct MonsterJson {
    id: Option<String>,
    source: Option<String>,
    #[serde(rename = "type")]
    monster_type: Option<String>,
    size: Option<String>,
    alignment: Option<String>,
    challenge_rating: Option<f32>,
    armor_class: Option<i32>,
    max_hit_points: Option<i32>,
    hit_dice: Option<String>,
    abilities: Option<AbilitiesJson>,
    speeds: Option<SpeedsJson>,
    #[serde(default)]
    skills: HashMap<String, String>,
    #[serde(default)]
    damage_affinities: HashMap<String, String>,
    #[serde(default)]
    condition_immunities: HashMap<String, bool>,
    translations: Option<HashMap<String, TranslationJson>>,
}

#[derive(Deserialize)]
struct SpeedsJson {
    walk: Option<i32>,
    fly: Option<i32>,
    swim: Option<i32>,
    climb: Option<i32>,
    burrow: Option<i32>,
    hover: Option<bool>,
}

#[derive(Deserialize)]
struct AbilitiesJson {
    str: Option<AbilityJson>,
    dex: Option<AbilityJson>,
    con: Option<AbilityJson>,
    int: Option<AbilityJson>,
    wis: Option<AbilityJson>,
    cha: Option<AbilityJson>,
}

#[derive(Deserialize)]
#[serde(rename_all = "camelCase")]
struct AbilityJson {
    value: Option<i32>,
    saving_throw_proficiency: Option<String>,
}

#[derive(Deserialize)]
struct TranslationJson {
    name: Option<String>,
    subtype: Option<String>,
    description: Option<String>,
    senses: Option<String>,
    languages: Option<Vec<String>>,
}

#[derive(Deserialize)]
struct MagicalItemJson {
    id: Option<String>,
    source: Option<String>,
    #[serde(rename = "type")]
    item_type: Option<String>,
    rarity: Option<String>,
    attunement: Option<bool>,
    translations: Option<HashMap<String, MagicalItemTranslationJson>>,
}

#[derive(Deserialize)]
struct MagicalItemTranslationJson {
    name: Option<String>,
    subtype: Option<String>,
    description: Option<String>,
}

// ── Converters ────────────────────────────────────────────────────────────────

fn parse_spells(json: &str) -> Result<Vec<Spell>, Box<dyn std::error::Error>> {
    let raw: Vec<SpellJson> = serde_json::from_str(json)?;
    Ok(raw.into_iter().filter_map(spell_from_json).collect())
}

fn spell_from_json(raw: SpellJson) -> Option<Spell> {
    let id = raw.id.filter(|s| !s.is_empty()).or_else(|| {
        eprintln!("WARNING: skipping spell with missing id");
        None
    })?;
    let source = raw.source.or_else(|| {
        eprintln!("WARNING: skipping spell '{id}': missing source");
        None
    })?;
    let level = raw.level.or_else(|| {
        eprintln!("WARNING: skipping spell '{id}': missing level");
        None
    })?;
    let school = raw.school.or_else(|| {
        eprintln!("WARNING: skipping spell '{id}': missing school");
        None
    })?;
    let concentration = raw.concentration.or_else(|| {
        eprintln!("WARNING: skipping spell '{id}': missing concentration");
        None
    })?;
    let ritual = raw.ritual.or_else(|| {
        eprintln!("WARNING: skipping spell '{id}': missing ritual");
        None
    })?;
    let raw_components = raw.components.or_else(|| {
        eprintln!("WARNING: skipping spell '{id}': missing components");
        None
    })?;
    let available_classes = raw.available_classes.or_else(|| {
        eprintln!("WARNING: skipping spell '{id}': missing availableClasses");
        None
    })?;
    let translations = raw.translations.filter(|t| !t.is_empty()).or_else(|| {
        eprintln!("WARNING: skipping spell '{id}': missing translations");
        None
    })?;

    Some(Spell {
        id,
        source,
        level,
        school,
        concentration,
        ritual,
        components: SpellComponents {
            verbal: raw_components.verbal,
            somatic: raw_components.somatic,
            material: raw_components.material,
        },
        available_classes,
        translations,
    })
}

fn parse_monsters(json: &str) -> Result<Vec<Monster>, Box<dyn std::error::Error>> {
    let raw: Vec<MonsterJson> = serde_json::from_str(json)?;
    Ok(raw.into_iter().filter_map(monster_from_json).collect())
}

fn monster_from_json(raw: MonsterJson) -> Option<Monster> {
    let id = raw.id.filter(|s| !s.is_empty()).or_else(|| {
        eprintln!("WARNING: skipping monster with missing id");
        None
    })?;
    let source = raw.source.or_else(|| {
        eprintln!("WARNING: skipping monster '{id}': missing source");
        None
    })?;
    let monster_type = raw.monster_type.or_else(|| {
        eprintln!("WARNING: skipping monster '{id}': missing type");
        None
    })?;
    let size = raw.size.or_else(|| {
        eprintln!("WARNING: skipping monster '{id}': missing size");
        None
    })?;
    let alignment = raw.alignment.or_else(|| {
        eprintln!("WARNING: skipping monster '{id}': missing alignment");
        None
    })?;
    let api_abilities = raw.abilities.or_else(|| {
        eprintln!("WARNING: skipping monster '{id}': missing abilities");
        None
    })?;
    let raw_translations = raw.translations.filter(|t| !t.is_empty()).or_else(|| {
        eprintln!("WARNING: skipping monster '{id}': missing translations");
        None
    })?;

    let translations: HashMap<String, Translation> = raw_translations
        .into_iter()
        .filter_map(|(locale, t)| {
            let name = t.name.or_else(|| {
                eprintln!("WARNING: monster '{id}' locale '{locale}': missing name");
                None
            })?;
            let description = t.description.or_else(|| {
                eprintln!("WARNING: monster '{id}' locale '{locale}': missing description");
                None
            })?;
            let senses = t.senses.or_else(|| {
                eprintln!("WARNING: monster '{id}' locale '{locale}': missing senses");
                None
            })?;
            Some((locale, Translation {
                name,
                subtype: t.subtype,
                description,
                senses,
                languages: t.languages.unwrap_or_default(),
            }))
        })
        .collect();

    if translations.is_empty() {
        eprintln!("WARNING: skipping monster '{id}': no valid translations");
        return None;
    }

    Some(Monster {
        id,
        source,
        monster_type,
        size,
        alignment,
        challenge_rating: raw.challenge_rating.unwrap_or(0.0),
        armor_class: raw.armor_class.unwrap_or(10),
        max_hit_points: raw.max_hit_points.unwrap_or(0),
        hit_dice: raw.hit_dice.unwrap_or_default(),
        abilities: Abilities {
            str: ability_from_json(api_abilities.str),
            dex: ability_from_json(api_abilities.dex),
            con: ability_from_json(api_abilities.con),
            int: ability_from_json(api_abilities.int),
            wis: ability_from_json(api_abilities.wis),
            cha: ability_from_json(api_abilities.cha),
        },
        speeds: speeds_from_json(raw.speeds),
        skills: raw.skills,
        damage_affinities: raw.damage_affinities,
        condition_immunities: raw.condition_immunities,
        translations,
    })
}

fn speeds_from_json(raw: Option<SpeedsJson>) -> Speeds {
    match raw {
        None => Speeds { walk: None, fly: None, swim: None, climb: None, burrow: None, hover: false },
        Some(s) => Speeds {
            walk: s.walk,
            fly: s.fly,
            swim: s.swim,
            climb: s.climb,
            burrow: s.burrow,
            hover: s.hover.unwrap_or(false),
        },
    }
}

fn ability_from_json(raw: Option<AbilityJson>) -> Ability {
    match raw {
        Some(a) => Ability {
            value: a.value.unwrap_or(10),
            saving_throw_proficiency: a.saving_throw_proficiency,
        },
        None => Ability { value: 10, saving_throw_proficiency: None },
    }
}

fn parse_magical_items(json: &str) -> Result<Vec<MagicalItem>, Box<dyn std::error::Error>> {
    let raw: Vec<MagicalItemJson> = serde_json::from_str(json)?;
    Ok(raw.into_iter().filter_map(magical_item_from_json).collect())
}

fn magical_item_from_json(raw: MagicalItemJson) -> Option<MagicalItem> {
    let id = raw.id.filter(|s| !s.is_empty()).or_else(|| {
        eprintln!("WARNING: skipping magical item with missing id");
        None
    })?;
    let source = raw.source.or_else(|| {
        eprintln!("WARNING: skipping magical item '{id}': missing source");
        None
    })?;
    let item_type = raw.item_type.or_else(|| {
        eprintln!("WARNING: skipping magical item '{id}': missing type");
        None
    })?;
    let rarity = raw.rarity.or_else(|| {
        eprintln!("WARNING: skipping magical item '{id}': missing rarity");
        None
    })?;
    let raw_translations = raw.translations.filter(|t| !t.is_empty()).or_else(|| {
        eprintln!("WARNING: skipping magical item '{id}': missing translations");
        None
    })?;

    let translations = raw_translations
        .into_iter()
        .filter_map(|(locale, t)| {
            let name = t.name.or_else(|| {
                eprintln!("WARNING: magical item '{id}' locale '{locale}': missing name");
                None
            })?;
            let description = t.description.or_else(|| {
                eprintln!("WARNING: magical item '{id}' locale '{locale}': missing description");
                None
            })?;
            Some((locale, MagicalItemTranslation { name, subtype: t.subtype, description }))
        })
        .collect::<HashMap<_, _>>();

    if translations.is_empty() {
        eprintln!("WARNING: skipping magical item '{id}': no valid translations");
        return None;
    }

    Some(MagicalItem {
        id,
        source,
        item_type,
        rarity,
        attunement: raw.attunement.unwrap_or(false),
        translations,
    })
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn monster_from_json_valid() {
        let raw = MonsterJson {
            id: Some("goblin".into()),
            source: Some("mm2024".into()),
            monster_type: Some("humanoid".into()),
            size: Some("small".into()),
            alignment: Some("neutral_evil".into()),
            challenge_rating: Some(0.25),
            armor_class: Some(15),
            max_hit_points: Some(7),
            hit_dice: Some("2d6".into()),
            abilities: Some(AbilitiesJson {
                str: Some(AbilityJson { value: Some(8), saving_throw_proficiency: None }),
                dex: Some(AbilityJson { value: Some(14), saving_throw_proficiency: None }),
                con: Some(AbilityJson { value: Some(10), saving_throw_proficiency: None }),
                int: Some(AbilityJson { value: Some(10), saving_throw_proficiency: None }),
                wis: Some(AbilityJson { value: Some(8), saving_throw_proficiency: None }),
                cha: Some(AbilityJson { value: Some(8), saving_throw_proficiency: None }),
            }),
            skills: HashMap::new(),
            damage_affinities: HashMap::new(),
            condition_immunities: HashMap::new(),
            translations: Some(HashMap::from([(
                "en".into(),
                TranslationJson {
                    name: Some("Goblin".into()),
                    subtype: Some("Goblinoid".into()),
                    description: Some("A small creature.".into()),
                    speed: Some("30 ft.".into()),
                    senses: Some("Darkvision 60 ft.".into()),
                    languages: Some(vec!["Common".into(), "Goblin".into()]),
                },
            )])),
        };
        let monster = monster_from_json(raw).expect("should parse");
        assert_eq!(monster.id, "goblin");
        assert_eq!(monster.monster_type, "humanoid");
        assert_eq!(monster.abilities.str.value, 8);
        assert_eq!(monster.translations["en"].name, "Goblin");
        assert_eq!(monster.translations["en"].subtype, Some("Goblinoid".into()));
    }

    #[test]
    fn monster_from_json_missing_id_returns_none() {
        let raw = MonsterJson {
            id: None,
            source: None,
            monster_type: None,
            size: None,
            alignment: None,
            challenge_rating: None,
            armor_class: None,
            max_hit_points: None,
            hit_dice: None,
            abilities: None,
            skills: HashMap::new(),
            damage_affinities: HashMap::new(),
            condition_immunities: HashMap::new(),
            translations: None,
        };
        assert!(monster_from_json(raw).is_none());
    }

    #[test]
    fn ability_from_json_defaults_to_10_when_none() {
        let ability = ability_from_json(None);
        assert_eq!(ability.value, 10);
        assert!(ability.saving_throw_proficiency.is_none());
    }
}
