#!/usr/bin/env bash
set -euo pipefail

readonly PLAY_NOTES_MAX_CHARS=500

sed -E \
    -e '/^## /d' \
    -e 's/ \(\[[^]]*\]\([^)]*\)\)//g' \
    -e 's/\*\*[^*]+:\*\* //' \
    -e 's/^### //' \
    -e 's/^\* /- /' \
    | sed '/^$/d' \
    | awk -v max="$PLAY_NOTES_MAX_CHARS" '
        total + length($0) + 1 > max { print substr($0, 1, max - total - 1); exit }
        { total += length($0) + 1; print }'
