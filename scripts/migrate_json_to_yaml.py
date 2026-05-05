#!/usr/bin/env python3
"""
One-time migration script: converts the three monolithic JSON compendium files
into one YAML file per entity.

Descriptions are kept as-is (HTML). Migrating descriptions to Markdown is
deferred to Phase 2, alongside the Compose renderer update.

Usage:
    pip install pyyaml
    python scripts/migrate_json_to_yaml.py

Output: data/compendium/spells/, data/compendium/monsters/, data/compendium/magical-items/

The existing JSON files are left untouched. After reviewing the YAML output,
run scripts/build_compendium.py to regenerate the JSON from YAML and verify round-trip fidelity.
"""

import json
import os
import sys

try:
    import yaml
except ImportError:
    sys.exit("Missing dependency: pip install pyyaml")


class _BlockScalarDumper(yaml.Dumper):
    """PyYAML dumper that renders multiline strings as literal block scalars (|)."""


def _str_representer(dumper: yaml.Dumper, data: str) -> yaml.ScalarNode:
    if "\n" in data:
        return dumper.represent_scalar("tag:yaml.org,2002:str", data, style="|")
    return dumper.represent_scalar("tag:yaml.org,2002:str", data)


_BlockScalarDumper.add_representer(str, _str_representer)

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(REPO_ROOT, "data", "compendium")


def write_yaml(path: str, data: dict) -> None:
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        yaml.dump(
            data,
            f,
            Dumper=_BlockScalarDumper,
            allow_unicode=True,
            default_flow_style=False,
            sort_keys=False,
        )


def migrate_type(type_name: str, id_field: str = "id") -> int:
    src = os.path.join(DATA_DIR, f"{type_name}.json")
    out_dir = os.path.join(DATA_DIR, type_name)
    with open(src, encoding="utf-8") as f:
        entities = json.load(f)

    for entity in entities:
        dest = os.path.join(out_dir, f"{entity[id_field]}.yaml")
        write_yaml(dest, entity)

    print(f"  {type_name}: {len(entities)} files written to {out_dir}/")
    return len(entities)


def main() -> None:
    print("Migrating compendium JSON → YAML...")
    total = 0
    total += migrate_type("spells")
    total += migrate_type("monsters")
    total += migrate_type("magical-items")
    print(f"\nDone. {total} YAML files created.")
    print("\nNext steps:")
    print("  1. Review a sample of generated YAML files (especially descriptions).")
    print("  2. Run: python scripts/build_compendium.py")
    print("  3. Verify the rebuilt JSON matches the originals.")


if __name__ == "__main__":
    main()
