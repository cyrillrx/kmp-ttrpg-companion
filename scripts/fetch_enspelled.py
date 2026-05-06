#!/usr/bin/env python3
"""Fetch EN descriptions for enspelled items from wikidot and patch their YAML files."""

import os
import re
import urllib.request

ITEMS_DIR = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "data", "compendium", "magical-items")
SLUGS = ["enspelled-armor", "enspelled-staff", "enspelled-weapon"]


def fetch(url):
    req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
    with urllib.request.urlopen(req, timeout=10) as r:
        return r.read().decode("utf-8", errors="replace")


def extract_wikidot(html):
    m = re.search(r'<div id="page-content">(.*?)\n</div>', html, re.DOTALL)
    if not m:
        m = re.search(r"<div id='page-content'>(.*?)\n</div>", html, re.DOTALL)
    if not m:
        return None
    content = m.group(1).strip()
    content = re.sub(r"<div class=[\"']page-tags.*", "", content, flags=re.DOTALL)
    return content.strip()


def patch_yaml(path, locale, description):
    with open(path, encoding="utf-8") as f:
        content = f.read()
    locale_header = f"  {locale}:\n"
    if locale_header not in content:
        return False
    locale_idx = content.index(locale_header)
    after = content[locale_idx:]
    marker = "    description: ''"
    if marker not in after:
        return False
    desc_idx = after.index(marker)
    eol = after.index("\n", desc_idx) + 1
    indented = description.replace("\n", "\n      ")
    new_desc = f"    description: |\n      {indented}"
    patched = content[:locale_idx] + after[:desc_idx] + new_desc + "\n" + after[eol:]
    with open(path, "w", encoding="utf-8") as f:
        f.write(patched)
    return True


for slug in SLUGS:
    url = f"http://dnd2024.wikidot.com/magic-item:{slug}"
    html = fetch(url)
    desc = extract_wikidot(html)
    if not desc:
        print(f"{slug}: could not extract description")
        continue
    path = os.path.join(ITEMS_DIR, f"{slug}.yaml")
    ok = patch_yaml(path, "en", desc)
    print(f"{slug}: EN patched={ok} ({len(desc)} chars)")
