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

    let addr = SocketAddr::from(([0, 0, 0, 0], 3000));
    println!("Listening on {addr}");
    let listener = tokio::net::TcpListener::bind(addr).await.unwrap();
    axum::serve(listener, app).await.unwrap();
}
