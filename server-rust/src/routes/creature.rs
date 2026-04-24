use std::sync::Arc;

use axum::{extract::State, Json};

use crate::{app_state::AppState, models::Creature, store::CompendiumStore};

pub async fn list_creatures<S: CompendiumStore>(
    State(state): State<Arc<AppState<S>>>,
) -> Json<Vec<Creature>> {
    Json(state.store.get_creatures().to_vec())
}
