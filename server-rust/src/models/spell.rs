use std::collections::HashMap;

use serde::{Deserialize, Serialize};

#[derive(Serialize, Clone, Debug)]
#[serde(rename_all = "camelCase")]
pub struct Spell {
    pub id: String,
    pub source: String,
    pub level: i32,
    pub school: String,
    pub concentration: bool,
    pub ritual: bool,
    pub components: SpellComponents,
    pub available_classes: Vec<String>,
    pub translations: HashMap<String, SpellTranslation>,
}

#[derive(Serialize, Clone, Debug)]
pub struct SpellComponents {
    pub verbal: bool,
    pub somatic: bool,
    pub material: bool,
}

#[derive(Serialize, Deserialize, Clone, Debug)]
#[serde(rename_all = "camelCase")]
pub struct SpellTranslation {
    pub name: String,
    pub casting_time: String,
    pub range: String,
    pub duration: String,
    pub material_description: Option<String>,
    pub description: String,
}
