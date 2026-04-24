use std::sync::Arc;

use axum::{extract::State, Json};

use crate::{app_state::AppState, models::MagicalItem, store::CompendiumStore};

pub async fn list_magical_items<S: CompendiumStore>(
    State(state): State<Arc<AppState<S>>>,
) -> Json<Vec<MagicalItem>> {
    Json(state.store.get_magical_items().to_vec())
}
