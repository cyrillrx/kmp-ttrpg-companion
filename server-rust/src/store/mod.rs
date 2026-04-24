pub mod json_store;

use crate::models::{Creature, MagicalItem, Spell};

pub trait CompendiumStore: Send + Sync {
    fn get_spells(&self) -> &[Spell];
    fn get_creatures(&self) -> &[Creature];
    fn get_magical_items(&self) -> &[MagicalItem];
}
