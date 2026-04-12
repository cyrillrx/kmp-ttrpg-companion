pub mod creature;
pub mod magical_item;
pub mod spell;

use std::sync::Arc;

use axum::{routing::get, Router};
use tower_http::cors::CorsLayer;

use crate::{app_state::AppState, store::CompendiumStore};

pub fn create_router<S: CompendiumStore + 'static>(state: AppState<S>) -> Router {
    Router::new()
        .route("/compendium/spells", get(spell::list_spells))
        .route("/compendium/creatures", get(creature::list_creatures))
        .route("/compendium/magical-items", get(magical_item::list_magical_items))
        .with_state(Arc::new(state))
        .layer(CorsLayer::permissive())
}
