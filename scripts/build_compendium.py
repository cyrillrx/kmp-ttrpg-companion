#!/usr/bin/env python3
"""
Build script: aggregates YAML source files into the three JSON compendium files
consumed by the KMP app and the Rust server.

Usage:
    pip install pyyaml markdown
    python scripts/build_compendium.py

Output:
    data/compendium/spells.json
    data/compendium/monsters.json
    data/compendium/magical-items.json
    cmp-ttrpg-companion/composeApp/src/commonMain/composeResources/files/spells.json
    cmp-ttrpg-companion/composeApp/src/commonMain/composeResources/files/monsters.json
    cmp-ttrpg-companion/composeApp/src/commonMain/composeResources/files/magical-items.json

CI mode (--check):
    Verifies that the committed JSON files are up to date with the YAML source.
    Exits with code 1 if any file differs.
"""

import argparse
import copy
import glob
import json
import os
import shutil
import sys

try:
    import yaml
except ImportError:
    sys.exit("Missing dependency: pip install pyyaml")

try:
    import markdown as markdown_lib
except ImportError:
    sys.exit("Missing dependency: pip install markdown")

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(REPO_ROOT, "data", "compendium")
APP_RESOURCES_DIR = os.path.join(
    REPO_ROOT,
    "cmp-ttrpg-companion",
    "composeApp",
    "src",
    "commonMain",
    "composeResources",
    "files",
)

_MD_EXTENSIONS = ["tables", "nl2br"]


def markdown_to_html(text: str | None) -> str | None:
    if not text:
        return text
    return markdown_lib.markdown(text, extensions=_MD_EXTENSIONS)


def load_yaml_dir(directory: str) -> list[dict]:
    pattern = os.path.join(directory, "*.yaml")
    paths = sorted(glob.glob(pattern))
    if not paths:
        print(f"  WARNING: no YAML files found in {directory}", file=sys.stderr)
    entities = []
    for path in paths:
        with open(path, encoding="utf-8") as f:
            entity = yaml.safe_load(f)
        entities.append(entity)
    return entities


def convert_descriptions_to_html(entities: list[dict]) -> list[dict]:
    """Return a deep copy with all description fields converted Markdown → HTML."""
    result = []
    for entity in entities:
        e = copy.deepcopy(entity)
        for locale, t in (e.get("translations") or {}).items():
            if t.get("description"):
                t["description"] = markdown_to_html(t["description"])
        result.append(e)
    return result


def write_json(path: str, data: list[dict]) -> None:
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
        f.write("\n")


def check_json(path: str, data: list[dict]) -> bool:
    """Return True if the file on disk matches `data`."""
    if not os.path.exists(path):
        print(f"  MISSING: {path}")
        return False
    with open(path, encoding="utf-8") as f:
        on_disk = json.load(f)
    if on_disk != data:
        print(f"  OUT OF DATE: {path}")
        return False
    return True


def build_type(type_name: str, check: bool) -> tuple[int, bool]:
    src_dir = os.path.join(DATA_DIR, type_name)
    out_path = os.path.join(DATA_DIR, f"{type_name}.json")
    app_path = os.path.join(APP_RESOURCES_DIR, f"{type_name}.json")

    entities = load_yaml_dir(src_dir)
    converted = convert_descriptions_to_html(entities)

    if check:
        ok = check_json(out_path, converted)
        return len(entities), ok

    write_json(out_path, converted)
    shutil.copy2(out_path, app_path)
    print(f"  {type_name}: {len(entities)} entities → {out_path}")
    return len(entities), True


def main() -> None:
    parser = argparse.ArgumentParser(description="Build compendium JSON from YAML source.")
    parser.add_argument(
        "--check",
        action="store_true",
        help="Verify committed JSON files are up to date (CI mode). Exit 1 if not.",
    )
    args = parser.parse_args()

    mode = "Checking" if args.check else "Building"
    print(f"{mode} compendium...")

    total = 0
    all_ok = True
    for type_name in ("spells", "monsters", "magical-items"):
        count, ok = build_type(type_name, check=args.check)
        total += count
        all_ok = all_ok and ok

    if args.check:
        if all_ok:
            print(f"\nOK — all JSON files are up to date ({total} entities).")
        else:
            print("\nFAIL — run `python scripts/build_compendium.py` and commit the result.")
            sys.exit(1)
    else:
        print(f"\nDone. {total} entities written.")


if __name__ == "__main__":
    main()
