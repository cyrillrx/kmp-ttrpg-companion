use std::collections::HashMap;

use serde::Serialize;

#[derive(Serialize, Clone, Debug)]
#[serde(rename_all = "camelCase")]
pub struct MagicalItem {
    pub id: String,
    pub source: String,
    #[serde(rename = "type")]
    pub item_type: String,
    pub rarity: String,
    pub attunement: bool,
    pub translations: HashMap<String, MagicalItemTranslation>,
}

#[derive(Serialize, Clone, Debug)]
pub struct MagicalItemTranslation {
    pub name: String,
    pub subtype: Option<String>,
    pub description: String,
}
