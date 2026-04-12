use crate::store::CompendiumStore;

pub struct AppState<S: CompendiumStore> {
    pub store: S,
}

impl<S: CompendiumStore> AppState<S> {
    pub fn new(store: S) -> Self {
        Self { store }
    }
}
