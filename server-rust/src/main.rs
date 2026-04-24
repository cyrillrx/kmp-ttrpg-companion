mod app_state;
mod models;
mod routes;
mod store;

use std::net::SocketAddr;

use app_state::AppState;
use store::json_store::JsonCompendiumStore;

#[tokio::main]
async fn main() {
    let data_dir =
        std::env::var("DATA_DIR").unwrap_or_else(|_| "../data/compendium".to_string());

    let store = JsonCompendiumStore::load(&data_dir)
        .unwrap_or_else(|e| panic!("Failed to load compendium data from '{data_dir}': {e}"));

    let state = AppState::new(store);
    let app = routes::create_router(state);

    let port: u16 = std::env::var("PORT")
        .ok()
        .and_then(|p| p.parse().ok())
        .unwrap_or(3000);
    let addr = SocketAddr::from(([0, 0, 0, 0], port));
    println!("Listening on {addr}");
    let listener = tokio::net::TcpListener::bind(addr).await.unwrap();
    axum::serve(listener, app).await.unwrap();
}
