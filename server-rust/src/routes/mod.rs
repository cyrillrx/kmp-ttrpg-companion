pub mod monster;
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
        .route("/compendium/monsters", get(monster::list_monsters))
        .route("/compendium/magical-items", get(magical_item::list_magical_items))
        .with_state(Arc::new(state))
        .layer(build_cors_layer())
}

#[cfg(test)]
mod tests {
    use super::*;
    use axum::{body::Body, http::{Request, StatusCode}};
    use tower::ServiceExt;

    use crate::models::{Monster, MagicalItem, Spell};

    struct MockStore;

    impl CompendiumStore for MockStore {
        fn get_spells(&self) -> &[Spell] { &[] }
        fn get_monsters(&self) -> &[Monster] { &[] }
        fn get_magical_items(&self) -> &[MagicalItem] { &[] }
    }

    fn mock_router() -> Router {
        create_router(AppState::new(MockStore))
    }

    #[tokio::test]
    async fn health_returns_200() {
        let res = mock_router()
            .oneshot(Request::builder().uri("/health").body(Body::empty()).unwrap())
            .await
            .unwrap();
        assert_eq!(res.status(), StatusCode::OK);
    }

    #[tokio::test]
    async fn list_spells_returns_200() {
        let res = mock_router()
            .oneshot(Request::builder().uri("/compendium/spells").body(Body::empty()).unwrap())
            .await
            .unwrap();
        assert_eq!(res.status(), StatusCode::OK);
    }

    #[tokio::test]
    async fn list_monsters_returns_200() {
        let res = mock_router()
            .oneshot(Request::builder().uri("/compendium/monsters").body(Body::empty()).unwrap())
            .await
            .unwrap();
        assert_eq!(res.status(), StatusCode::OK);
    }

    #[tokio::test]
    async fn list_magical_items_returns_200() {
        let res = mock_router()
            .oneshot(Request::builder().uri("/compendium/magical-items").body(Body::empty()).unwrap())
            .await
            .unwrap();
        assert_eq!(res.status(), StatusCode::OK);
    }

    #[tokio::test]
    async fn unknown_route_returns_404() {
        let res = mock_router()
            .oneshot(Request::builder().uri("/unknown").body(Body::empty()).unwrap())
            .await
            .unwrap();
        assert_eq!(res.status(), StatusCode::NOT_FOUND);
    }
}
