use serde::Serialize;

#[derive(Serialize, Clone)]
#[serde(rename_all = "camelCase")]
pub struct Abilities {
    pub str: i32,
    pub dex: i32,
    pub con: i32,
    pub int: i32,
    pub wis: i32,
    pub cha: i32,
}

#[derive(Serialize, Clone)]
#[serde(rename_all = "camelCase")]
pub struct Creature {
    pub id: String,
    pub name: String,
    pub description: String,
    #[serde(rename = "type")]
    pub creature_type: String,
    pub subtype: String,
    pub size: String,
    pub alignment: String,
    pub challenge_rating: f32,
    pub armor_class: i32,
    pub max_hit_points: i32,
    pub speed: String,
    pub languages: Vec<String>,
    pub abilities: Abilities,
}
