use std::sync::Arc;

use axum::{extract::State, Json};

use crate::{app_state::AppState, models::Spell, store::CompendiumStore};

pub async fn list_spells<S: CompendiumStore>(
    State(state): State<Arc<AppState<S>>>,
) -> Json<Vec<Spell>> {
    Json(state.store.get_spells().to_vec())
}
