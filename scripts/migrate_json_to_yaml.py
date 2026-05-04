#!/usr/bin/env python3
"""
One-time migration script: converts the three monolithic JSON compendium files
into one YAML file per entity, with descriptions converted from HTML to Markdown.

Usage:
    pip install pyyaml html2text
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

try:
    import html2text
except ImportError:
    sys.exit("Missing dependency: pip install html2text")

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(REPO_ROOT, "data", "compendium")

_md_converter = html2text.HTML2Text()
_md_converter.body_width = 0       # no line wrapping
_md_converter.protect_links = True
_md_converter.wrap_links = False


def html_to_markdown(html: str | None) -> str | None:
    if not html:
        return html
    md = _md_converter.handle(html).strip()
    return md if md else None


def write_yaml(path: str, data: dict) -> None:
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        yaml.dump(
            data,
            f,
            allow_unicode=True,
            default_flow_style=False,
            sort_keys=False,
            width=10000,  # avoid yaml wrapping long strings mid-line
        )


def migrate_spells() -> int:
    src = os.path.join(DATA_DIR, "spells.json")
    out_dir = os.path.join(DATA_DIR, "spells")
    with open(src, encoding="utf-8") as f:
        spells = json.load(f)

    for spell in spells:
        for locale, t in (spell.get("translations") or {}).items():
            if t.get("description"):
                t["description"] = html_to_markdown(t["description"])

        dest = os.path.join(out_dir, f"{spell['id']}.yaml")
        write_yaml(dest, spell)

    print(f"  spells: {len(spells)} files written to {out_dir}/")
    return len(spells)


def migrate_monsters() -> int:
    src = os.path.join(DATA_DIR, "monsters.json")
    out_dir = os.path.join(DATA_DIR, "monsters")
    with open(src, encoding="utf-8") as f:
        monsters = json.load(f)

    for monster in monsters:
        for locale, t in (monster.get("translations") or {}).items():
            if t.get("description"):
                t["description"] = html_to_markdown(t["description"])

        dest = os.path.join(out_dir, f"{monster['id']}.yaml")
        write_yaml(dest, monster)

    print(f"  monsters: {len(monsters)} files written to {out_dir}/")
    return len(monsters)


def migrate_magical_items() -> int:
    src = os.path.join(DATA_DIR, "magical-items.json")
    out_dir = os.path.join(DATA_DIR, "magical-items")
    with open(src, encoding="utf-8") as f:
        items = json.load(f)

    for item in items:
        for locale, t in (item.get("translations") or {}).items():
            if t.get("description"):
                t["description"] = html_to_markdown(t["description"])

        dest = os.path.join(out_dir, f"{item['id']}.yaml")
        write_yaml(dest, item)

    print(f"  magical-items: {len(items)} files written to {out_dir}/")
    return len(items)


def main() -> None:
    print("Migrating compendium JSON → YAML...")
    total = 0
    total += migrate_spells()
    total += migrate_monsters()
    total += migrate_magical_items()
    print(f"\nDone. {total} YAML files created.")
    print("\nNext steps:")
    print("  1. Review a sample of generated YAML files (especially descriptions).")
    print("  2. Run: python scripts/build_compendium.py")
    print("  3. Verify the rebuilt JSON matches the originals.")


if __name__ == "__main__":
    main()
