use serde::Serialize;

#[derive(Serialize, Clone, Debug)]
#[serde(rename_all = "camelCase")]
pub struct MagicalItem {
    pub id: String,
    pub title: String,
    pub description: String,
    #[serde(rename = "type")]
    pub item_type: String,
    pub rarity: String,
    pub attunement: bool,
}
