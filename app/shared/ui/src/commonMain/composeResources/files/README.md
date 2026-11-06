# Compendium JSON files — do not edit

The JSON files in this directory are **symlinks** to `data/compendium/*.json` in the repository root:

```
spells.json        → ../../../../../../../../data/compendium/spells.json
monsters.json      → ../../../../../../../../data/compendium/monsters.json
magical-items.json → ../../../../../../../../data/compendium/magical-items.json
```

**Do not copy, replace, or edit these files directly.**  
They are generated artifacts. The source of truth is `data/compendium/{type}/*.yaml`.

To update the compendium data:
1. Edit (or add) YAML files in `data/compendium/{spells,monsters,magical-items}/`
2. Run `python scripts/build_compendium.py` from the repository root
3. Commit both the YAML changes and the regenerated JSON

See [`docs/adr/adr-002-compendium-source-format.md`](../../../../../../docs/adr/adr-002-compendium-source-format.md) for the full rationale.
