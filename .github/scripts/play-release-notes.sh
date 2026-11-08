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
    | cut -c "1-$PLAY_NOTES_MAX_CHARS" \
    | awk -v max="$PLAY_NOTES_MAX_CHARS" '{ total += length($0) + 1; if (total > max) exit; print }'
