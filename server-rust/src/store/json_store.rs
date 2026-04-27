use std::path::Path;

use serde::Deserialize;
use serde_json::Value;

use crate::models::{
    creature::{Abilities, Creature},
    magical_item::MagicalItem,
    spell::{Spell, SpellComponents, SpellTranslation},
};
use crate::store::CompendiumStore;

pub struct JsonCompendiumStore {
    spells: Vec<Spell>,
    creatures: Vec<Creature>,
    magical_items: Vec<MagicalItem>,
}

impl JsonCompendiumStore {
    pub fn load(data_dir: &str) -> Result<Self, Box<dyn std::error::Error>> {
        let base = Path::new(data_dir);

        let spells_json = std::fs::read_to_string(base.join("spells.json"))?;
        let creatures_json = std::fs::read_to_string(base.join("creatures.json"))?;
        let items_json = std::fs::read_to_string(base.join("magical-items.json"))?;

        let spells = parse_spells(&spells_json)?;
        let creatures = parse_creatures(&creatures_json)?;
        let magical_items = parse_magical_items(&items_json)?;

        Ok(Self { spells, creatures, magical_items })
    }
}

impl CompendiumStore for JsonCompendiumStore {
    fn get_spells(&self) -> &[Spell] {
        &self.spells
    }

    fn get_creatures(&self) -> &[Creature] {
        &self.creatures
    }

    fn get_magical_items(&self) -> &[MagicalItem] {
        &self.magical_items
    }
}

// ── Raw JSON structs ──────────────────────────────────────────────────────────

#[derive(Deserialize)]
struct SpellJson {
    id: Option<String>,
    source: Option<String>,
    level: Option<i32>,
    school: Option<String>,
    concentration: Option<bool>,
    ritual: Option<bool>,
    components: Option<SpellComponentsJson>,
    available_classes: Option<Vec<String>>,
    translations: Option<std::collections::HashMap<String, SpellTranslation>>,
}

#[derive(Deserialize)]
struct SpellComponentsJson {
    verbal: bool,
    somatic: bool,
    material: bool,
}

#[derive(Deserialize)]
struct CreatureJson {
    title: Option<String>,
    content: Option<String>,
    truetype: Option<String>,
    size: Option<String>,
    alignment: Option<String>,
    #[serde(default)]
    challenge: Value,
    header: Option<CreatureHeaderJson>,
}

#[derive(Deserialize)]
struct CreatureHeaderJson {
    monster: Option<MonsterJson>,
}

#[derive(Deserialize)]
struct MonsterJson {
    #[serde(default)]
    ac: Value,
    hp: Option<String>,
    speed: Option<String>,
    #[serde(rename = "str")]
    strength: Option<String>,
    dex: Option<String>,
    con: Option<String>,
    #[serde(rename = "int")]
    intelligence: Option<String>,
    wis: Option<String>,
    cha: Option<String>,
    languages: Option<String>,
}

#[derive(Deserialize)]
struct MagicalItemJson {
    title: Option<String>,
    content: Option<String>,
    #[serde(rename = "type")]
    item_type: Option<String>,
    rarity: Option<String>,
    attunement: Option<String>,
}

// ── Parsing helpers ───────────────────────────────────────────────────────────

fn parse_ability_score(s: &str) -> i32 {
    s.split_whitespace()
        .next()
        .and_then(|n| n.parse().ok())
        .unwrap_or(10)
}

fn parse_hp(s: &str) -> i32 {
    s.split_whitespace()
        .next()
        .and_then(|n| n.parse().ok())
        .unwrap_or(0)
}

fn parse_challenge(v: &Value) -> f32 {
    match v {
        Value::Number(n) => n.as_f64().unwrap_or(0.0) as f32,
        Value::String(s) => s.parse().unwrap_or(0.0),
        _ => 0.0,
    }
}

fn parse_ac(v: &Value) -> i32 {
    match v {
        Value::Number(n) => n.as_i64().unwrap_or(10) as i32,
        Value::String(s) => s
            .split_whitespace()
            .next()
            .and_then(|n| n.parse().ok())
            .unwrap_or(10),
        _ => 10,
    }
}

/// Parses "Humanoïde (Goblinoïde)" into ("Humanoïde", "Goblinoïde").
fn parse_type_subtype(truetype: &str) -> (String, String) {
    match truetype.find('(') {
        Some(idx) => {
            let creature_type = truetype[..idx].trim().to_string();
            let subtype = truetype[idx + 1..]
                .trim_end_matches(')')
                .trim()
                .to_string();
            (creature_type, subtype)
        }
        None => (truetype.trim().to_string(), String::new()),
    }
}

fn parse_languages(s: &str) -> Vec<String> {
    s.split(',')
        .map(|l| l.trim().to_string())
        .filter(|l| !l.is_empty())
        .collect()
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

fn parse_creatures(json: &str) -> Result<Vec<Creature>, Box<dyn std::error::Error>> {
    let raw: Vec<CreatureJson> = serde_json::from_str(json)?;
    Ok(raw.into_iter().filter_map(creature_from_json).collect())
}

fn creature_from_json(raw: CreatureJson) -> Option<Creature> {
    let name = raw.title.filter(|t| !t.is_empty())?;
    let monster = raw.header.as_ref()?.monster.as_ref();

    let (creature_type, subtype) = raw
        .truetype
        .as_deref()
        .map(parse_type_subtype)
        .unwrap_or_default();

    let abilities = Abilities {
        str: monster
            .and_then(|m| m.strength.as_deref())
            .map(parse_ability_score)
            .unwrap_or(10),
        dex: monster
            .and_then(|m| m.dex.as_deref())
            .map(parse_ability_score)
            .unwrap_or(10),
        con: monster
            .and_then(|m| m.con.as_deref())
            .map(parse_ability_score)
            .unwrap_or(10),
        int: monster
            .and_then(|m| m.intelligence.as_deref())
            .map(parse_ability_score)
            .unwrap_or(10),
        wis: monster
            .and_then(|m| m.wis.as_deref())
            .map(parse_ability_score)
            .unwrap_or(10),
        cha: monster
            .and_then(|m| m.cha.as_deref())
            .map(parse_ability_score)
            .unwrap_or(10),
    };

    Some(Creature {
        id: name.clone(),
        name,
        description: raw.content.unwrap_or_default(),
        creature_type,
        subtype,
        size: raw.size.unwrap_or_default(),
        alignment: raw.alignment.unwrap_or_default(),
        challenge_rating: parse_challenge(&raw.challenge),
        armor_class: monster.map(|m| parse_ac(&m.ac)).unwrap_or(10),
        max_hit_points: monster
            .and_then(|m| m.hp.as_deref())
            .map(parse_hp)
            .unwrap_or(0),
        speed: monster
            .and_then(|m| m.speed.clone())
            .unwrap_or_default(),
        languages: monster
            .and_then(|m| m.languages.as_deref())
            .map(parse_languages)
            .unwrap_or_default(),
        abilities,
    })
}

fn parse_magical_items(json: &str) -> Result<Vec<MagicalItem>, Box<dyn std::error::Error>> {
    let raw: Vec<MagicalItemJson> = serde_json::from_str(json)?;
    Ok(raw.into_iter().filter_map(magical_item_from_json).collect())
}

fn magical_item_from_json(raw: MagicalItemJson) -> Option<MagicalItem> {
    let title = raw.title.filter(|t| !t.is_empty())?;

    Some(MagicalItem {
        id: title.clone(),
        title,
        description: raw.content.unwrap_or_default(),
        item_type: raw.item_type.unwrap_or_default(),
        rarity: raw.rarity.unwrap_or_default(),
        attunement: raw.attunement.is_some_and(|a| !a.is_empty()),
    })
}

#[cfg(test)]
mod tests {
    use super::*;
    use serde_json::json;

    #[test]
    fn parse_type_subtype_with_subtype() {
        let (t, s) = parse_type_subtype("Humanoïde (Goblinoïde)");
        assert_eq!(t, "Humanoïde");
        assert_eq!(s, "Goblinoïde");
    }

    #[test]
    fn parse_type_subtype_without_subtype() {
        let (t, s) = parse_type_subtype("Bête");
        assert_eq!(t, "Bête");
        assert_eq!(s, "");
    }

    #[test]
    fn parse_ability_score_with_modifier() {
        assert_eq!(parse_ability_score("16 (+3)"), 16);
        assert_eq!(parse_ability_score("8 (-1)"), 8);
    }

    #[test]
    fn parse_ability_score_empty_defaults_to_10() {
        assert_eq!(parse_ability_score(""), 10);
    }

    #[test]
    fn parse_hp_extracts_first_number() {
        assert_eq!(parse_hp("52 (8d8 + 16)"), 52);
    }

    #[test]
    fn parse_hp_empty_defaults_to_0() {
        assert_eq!(parse_hp(""), 0);
    }

    #[test]
    fn parse_challenge_from_number() {
        assert_eq!(parse_challenge(&json!(1.0)), 1.0f32);
        assert_eq!(parse_challenge(&json!(0)), 0.0f32);
    }

    #[test]
    fn parse_challenge_from_string() {
        assert_eq!(parse_challenge(&json!("0.5")), 0.5f32);
    }

    #[test]
    fn parse_challenge_from_null_defaults_to_0() {
        assert_eq!(parse_challenge(&json!(null)), 0.0f32);
    }

    #[test]
    fn parse_ac_from_number() {
        assert_eq!(parse_ac(&json!(15)), 15);
    }

    #[test]
    fn parse_ac_from_string_with_note() {
        assert_eq!(parse_ac(&json!("13 (armure naturelle)")), 13);
    }

    #[test]
    fn parse_ac_from_null_defaults_to_10() {
        assert_eq!(parse_ac(&json!(null)), 10);
    }

    #[test]
    fn parse_languages_splits_on_comma() {
        let langs = parse_languages("commun, gobelin");
        assert_eq!(langs, vec!["commun", "gobelin"]);
    }

    #[test]
    fn parse_languages_filters_empty() {
        let langs = parse_languages("");
        assert!(langs.is_empty());
    }
}
