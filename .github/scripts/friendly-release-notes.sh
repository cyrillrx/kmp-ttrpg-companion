#!/usr/bin/env bash
set -euo pipefail

readonly GEMINI_MODEL="gemini-3.5-flash-lite"
readonly GEMINI_ENDPOINT="https://generativelanguage.googleapis.com/v1beta/models/$GEMINI_MODEL:generateContent"
SCRIPT_DIR="$(dirname "$0")"
readonly SCRIPT_DIR
readonly INSTRUCTION="You rewrite the changelog of TTRPG Companion, an app for tabletop role-playing games, \
for family and friends who are not developers. Write one short line per change, starting with \"- \", \
in plain words: no technical terms, code names, issue numbers or links. Merge related changes, describe \
only what the changelog says and never invent a change. Keep each text under 450 characters. \
Return the text in English as \"en\" and in French as \"fr\"."

rewrite_for_testers() {
    jq -n --arg instruction "$INSTRUCTION" --arg changelog "$1" '{
        systemInstruction: { parts: [{ text: $instruction }] },
        contents: [{ role: "user", parts: [{ text: $changelog }] }],
        generationConfig: {
            responseMimeType: "application/json",
            responseJsonSchema: {
                type: "object",
                properties: { en: { type: "string" }, fr: { type: "string" } },
                required: ["en", "fr"]
            }
        }
    }' \
        | curl --fail --silent --show-error --max-time 60 \
            --header "Content-Type: application/json" \
            --header "x-goog-api-key: $GEMINI_API_KEY" \
            --data @- "$GEMINI_ENDPOINT" \
        | jq --exit-status '.candidates[0].content.parts[0].text | fromjson | select(.en != "" and .fr != "")'
}

write_output() {
    printf '%s<<RELEASE_NOTES_EOF\n%s\nRELEASE_NOTES_EOF\n' "$1" "$2"
}

changelog="$(cat)"

if [[ -n "${GEMINI_API_KEY:-}" ]] && notes="$(rewrite_for_testers "$changelog")"; then
    en="$(jq -r .en <<<"$notes")"
    fr="$(jq -r .fr <<<"$notes")"
    write_output play_en "$("$SCRIPT_DIR/play-release-notes.sh" <<<"$en")"
    write_output play_fr "$("$SCRIPT_DIR/play-release-notes.sh" <<<"$fr")"
    write_output summary "$(printf "### What's new\n\n%s\n\n### Nouveautés\n\n%s\n\n---" "$en" "$fr")"
else
    echo "::warning::No tester notes from Gemini: Play gets the technical changelog, and the GitHub Release is unchanged." >&2
    write_output play_en "$("$SCRIPT_DIR/play-release-notes.sh" <<<"$changelog")"
    write_output play_fr ""
    write_output summary ""
fi
