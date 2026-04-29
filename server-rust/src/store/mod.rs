pub mod json_store;

use crate::models::{Monster, MagicalItem, Spell};

pub trait CompendiumStore: Send + Sync {
    fn get_spells(&self) -> &[Spell];
    fn get_monsters(&self) -> &[Monster];
    fn get_magical_items(&self) -> &[MagicalItem];
}
