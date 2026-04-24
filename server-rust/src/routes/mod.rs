pub mod creature;
pub mod magical_item;
pub mod spell;

use std::sync::Arc;

use axum::{http::{HeaderValue, StatusCode}, routing::get, Router};
use tower_http::cors::{AllowOrigin, CorsLayer};

use crate::{app_state::AppState, store::CompendiumStore};

async fn health() -> StatusCode {
    StatusCode::OK
}

fn build_cors_layer() -> CorsLayer {
    let allowed: Vec<HeaderValue> = std::env::var("ALLOWED_ORIGINS")
        .unwrap_or_default()
        .split(',')
        .map(str::trim)
        .filter(|s| !s.is_empty())
        .filter_map(|s| s.parse().ok())
        .collect();

    if allowed.is_empty() {
        CorsLayer::permissive()
    } else {
        CorsLayer::new().allow_origin(AllowOrigin::list(allowed))
    }
}

pub fn create_router<S: CompendiumStore + 'static>(state: AppState<S>) -> Router {
    Router::new()
        .route("/health", get(health))
        .route("/compendium/spells", get(spell::list_spells))
        .route("/compendium/creatures", get(creature::list_creatures))
        .route("/compendium/magical-items", get(magical_item::list_magical_items))
        .with_state(Arc::new(state))
        .layer(build_cors_layer())
}
