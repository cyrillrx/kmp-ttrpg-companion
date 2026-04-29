use std::sync::Arc;

use axum::{extract::State, Json};

use crate::{app_state::AppState, models::Monster, store::CompendiumStore};

pub async fn list_monsters<S: CompendiumStore>(
    State(state): State<Arc<AppState<S>>>,
) -> Json<Vec<Monster>> {
    Json(state.store.get_monsters().to_vec())
}
