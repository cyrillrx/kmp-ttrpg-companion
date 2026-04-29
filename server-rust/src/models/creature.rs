use std::collections::HashMap;

use serde::Serialize;

#[derive(Serialize, Clone, Debug)]
#[serde(rename_all = "camelCase")]
pub struct Ability {
    pub value: i32,
    pub saving_throw_proficiency: Option<String>,
}

#[derive(Serialize, Clone, Debug)]
#[serde(rename_all = "camelCase")]
pub struct Abilities {
    pub str: Ability,
    pub dex: Ability,
    pub con: Ability,
    pub int: Ability,
    pub wis: Ability,
    pub cha: Ability,
}

#[derive(Serialize, Clone, Debug)]
#[serde(rename_all = "camelCase")]
pub struct Translation {
    pub name: String,
    pub subtype: Option<String>,
    pub description: String,
    pub speed: String,
    pub senses: String,
    pub languages: Vec<String>,
}

#[derive(Serialize, Clone, Debug)]
#[serde(rename_all = "camelCase")]
pub struct Monster {
    pub id: String,
    pub source: String,
    #[serde(rename = "type")]
    pub monster_type: String,
    pub size: String,
    pub alignment: String,
    pub challenge_rating: f32,
    pub armor_class: i32,
    pub max_hit_points: i32,
    pub hit_dice: String,
    pub abilities: Abilities,
    pub skills: HashMap<String, String>,
    pub damage_affinities: HashMap<String, String>,
    pub condition_immunities: HashMap<String, bool>,
    pub translations: HashMap<String, Translation>,
}
