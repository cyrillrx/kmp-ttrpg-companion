#!/usr/bin/env python3
"""
Build script: aggregates YAML source files into the three JSON compendium files
consumed by the KMP app and the Rust server.

Descriptions are kept as-is (HTML for now). Markdown conversion is deferred to
Phase 2, alongside the Compose renderer update.

Usage:
    pip install pyyaml
    python data/scripts/build_compendium.py

Output:
    data/compendium/spells.json
    data/compendium/monsters.json
    data/compendium/magical-items.json
    (app resource files are symlinks to the above — no copy needed)

CI mode (--check):
    Verifies that the committed JSON files are up to date with the YAML source.
    Exits with code 1 if any file differs.
"""

import argparse
import glob
import json
import os
import shutil
import sys

try:
    import yaml
except ImportError:
    sys.exit("Missing dependency: pip install pyyaml")


def _str_representer(dumper: yaml.Dumper, data: str) -> yaml.ScalarNode:
    if "\n" in data:
        return dumper.represent_scalar("tag:yaml.org,2002:str", data, style="|")
    return dumper.represent_scalar("tag:yaml.org,2002:str", data)


yaml.add_representer(str, _str_representer)

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
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


def fmt_collection(name: str) -> int:
    src_dir = os.path.join(DATA_DIR, name)
    yaml_files = sorted(glob.glob(os.path.join(src_dir, "*.yaml")))
    for path in yaml_files:
        with open(path, encoding="utf-8") as f:
            entity = yaml.safe_load(f)
        if entity is not None:
            with open(path, "w", encoding="utf-8") as f:
                yaml.dump(entity, f, allow_unicode=True, sort_keys=False, default_flow_style=False)
    print(f"  {name}: {len(yaml_files)} files formatted")
    return len(yaml_files)


def build_collection(name: str, check: bool) -> tuple[int, bool]:
    src_dir = os.path.join(DATA_DIR, name)
    out_path = os.path.join(DATA_DIR, f"{name}.json")
    app_path = os.path.join(APP_RESOURCES_DIR, f"{name}.json")

    yaml_files = sorted(glob.glob(os.path.join(src_dir, "*.yaml")))
    if not yaml_files:
        print(f"  WARNING: no YAML files found in {src_dir}", file=sys.stderr)
        if check:
            print(f"  SKIPPED: {name} (no YAML source files — migration pending)")
        else:
            print(f"  SKIPPED: {name} (no YAML source files — keeping existing JSON)")
        return 0, True

    entities = []
    for path in yaml_files:
        with open(path, encoding="utf-8") as f:
            entity = yaml.safe_load(f)
        if entity is not None:
            entities.append(entity)
        else:
            print(f"  WARNING: skipping empty file {path}", file=sys.stderr)

    if check:
        ok = check_json(out_path, entities)
        return len(entities), ok

    write_json(out_path, entities)
    # The app resource files are symlinks to data/compendium/ — no copy needed.
    if os.path.exists(app_path) and not os.path.samefile(out_path, app_path):
        shutil.copy2(out_path, app_path)
    print(f"  {name}: {len(entities)} entities → {out_path}")
    return len(entities), True


def main() -> None:
    parser = argparse.ArgumentParser(description="Build compendium JSON from YAML source.")
    group = parser.add_mutually_exclusive_group()
    group.add_argument(
        "--check",
        action="store_true",
        help="Verify committed JSON files are up to date (CI mode). Exit 1 if not.",
    )
    group.add_argument(
        "--fmt",
        action="store_true",
        help="Reformat all YAML source files in place (normalises block scalar style).",
    )
    args = parser.parse_args()

    collections = ("spells", "monsters", "magical-items", "pc-presets", "npc-presets")

    if args.fmt:
        print("Formatting YAML sources...")
        total = sum(fmt_collection(name) for name in collections)
        print(f"\nDone. {total} files formatted.")
        return

    mode = "Checking" if args.check else "Building"
    print(f"{mode} compendium...")

    total = 0
    all_ok = True
    for name in collections:
        count, ok = build_collection(name, check=args.check)
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
