#!/usr/bin/env python3
"""
One-shot script: fetch missing descriptions for magical items from aidedd.org.

For each item YAML with an empty description:
  1. Fetch https://www.aidedd.org/dnd/om.php?vo={id}  → EN description + vf= slug
  2. Fetch https://www.aidedd.org/dnd/om.php?vf={slug} → FR description
  3. Patch the YAML file in-place

Items not found on aidedd.org (e.g. new DMG 2024 entries) are reported but left untouched.

Usage:
    python3 scripts/fetch_missing_descriptions.py [--dry-run]
"""

import argparse
import glob
import os
import re
import sys
import time
import urllib.request

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
MAGICAL_ITEMS_DIR = os.path.join(REPO_ROOT, "data", "compendium", "magical-items")
BASE_URL = "https://www.aidedd.org/dnd/om.php"


def fetch_html(url: str) -> str | None:
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
        with urllib.request.urlopen(req, timeout=10) as resp:
            return resp.read().decode("utf-8", errors="replace")
    except Exception as e:
        print(f"  ERROR fetching {url}: {e}", file=sys.stderr)
        return None


def extract_vf_slug(html: str) -> str | None:
    """Extract the vf= slug from <link rel='alternate' hreflang='fr' href='...'>."""
    m = re.search(r"<link[^>]+hreflang=['\"]fr['\"][^>]+href=['\"][^'\"]*[?&]vf=([^&'\"]+)", html)
    if not m:
        m = re.search(r"<link[^>]+href=['\"][^'\"]*[?&]vf=([^&'\"]+)['\"][^>]+hreflang=['\"]fr['\"]", html)
    return m.group(1) if m else None


def extract_description(html: str) -> str | None:
    """Extract the content of <div class='description'> as raw HTML."""
    if "Something goes wrong" in html or "does not exist" in html.lower():
        return None
    m = re.search(r"<div class=['\"]description['\"]>(.*?)</div>", html, re.DOTALL)
    return m.group(1).strip() if m else None


def patch_yaml(path: str, locale: str, description: str, dry_run: bool) -> bool:
    """Replace description: '' for the given locale in a YAML file."""
    with open(path, encoding="utf-8") as f:
        content = f.read()

    # Find locale block then its description: '' line
    locale_header = f"  {locale}:\n"
    if locale_header not in content:
        return False

    locale_idx = content.index(locale_header)
    after_locale = content[locale_idx:]
    marker = "    description: ''"
    if marker not in after_locale:
        return False

    desc_idx = after_locale.index(marker)
    end_of_line = after_locale.index("\n", desc_idx) + 1
    indented = description.replace("\n", "\n      ")
    new_desc = f"    description: |\n      {indented}"
    patched = content[:locale_idx] + after_locale[:desc_idx] + new_desc + "\n" + after_locale[end_of_line:]

    if dry_run:
        print(f"  [dry-run] Would patch {locale} in {os.path.basename(path)}")
        return True

    with open(path, "w", encoding="utf-8") as f:
        f.write(patched)
    return True


def main():
    parser = argparse.ArgumentParser(description="Fetch missing magical item descriptions from aidedd.org")
    parser.add_argument("--dry-run", action="store_true", help="Print actions without writing files")
    args = parser.parse_args()

    paths = sorted(glob.glob(os.path.join(MAGICAL_ITEMS_DIR, "*.yaml")))
    to_fix = [p for p in paths if "description: ''" in open(p).read()]

    if not to_fix:
        print("No items with empty descriptions found.")
        return

    print(f"Found {len(to_fix)} items with empty descriptions.\n")
    skipped = []

    for path in to_fix:
        item_id = os.path.splitext(os.path.basename(path))[0]
        print(f"→ {item_id}")

        en_html = fetch_html(f"{BASE_URL}?vo={item_id}")
        if not en_html:
            print("  SKIP — fetch error")
            skipped.append(item_id)
            time.sleep(0.5)
            continue

        en_desc = extract_description(en_html)
        if not en_desc:
            print("  SKIP — not found on aidedd.org")
            skipped.append(item_id)
            time.sleep(0.5)
            continue

        vf_slug = extract_vf_slug(en_html)
        print(f"  EN: {len(en_desc)} chars | vf_slug={vf_slug!r}")
        patch_yaml(path, "en", en_desc, args.dry_run)

        if vf_slug:
            time.sleep(0.5)
            fr_html = fetch_html(f"{BASE_URL}?vf={vf_slug}")
            if fr_html:
                fr_desc = extract_description(fr_html)
                if fr_desc:
                    print(f"  FR: {len(fr_desc)} chars")
                    patch_yaml(path, "fr", fr_desc, args.dry_run)
                else:
                    print(f"  FR: not found at ?vf={vf_slug}")
            else:
                print("  FR: fetch error")
        else:
            print("  FR: no vf= link found on EN page")

        time.sleep(0.5)

    if skipped:
        print(f"\nSkipped ({len(skipped)}): {', '.join(skipped)}")
    else:
        print("\nDone — all items processed.")


if __name__ == "__main__":
    main()
