use serde::Serialize;

#[derive(Serialize, Clone, Debug)]
#[serde(rename_all = "camelCase")]
pub struct Spell {
    pub id: String,
    pub title: String,
    pub description: String,
    pub level: i32,
    pub casting_time: String,
    pub range: String,
    pub components: String,
    pub duration: String,
    pub schools: Vec<String>,
    pub available_classes: Vec<String>,
}
