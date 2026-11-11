#!/usr/bin/env bash
# Captures the Play Store screenshots in English and French on the connected phone.
# Usage: capture.sh [output dir] (default: build/store-screenshots)
# The app is reset first: its local data is lost. Maestro's output, with the uncropped
# screenshots, stays in <output dir>/raw.
set -euo pipefail

readonly APP_ID="${APP_ID:-com.cyrillrx.rpg}"
readonly FLOW="$(dirname "$0")/store-screenshots.yaml"
readonly OUTPUT_DIR="${1:-build/store-screenshots}"
readonly STATUS_BAR_HEIGHT=160

device="$(adb get-serialno)"
readonly device
stay_on_setting="$(adb shell settings get global stay_on_while_plugged_in)"
readonly stay_on_setting

restore_device() {
    adb shell cmd locale set-app-locales "$APP_ID" --locales ""
    adb shell settings put global stay_on_while_plugged_in "$stay_on_setting"
}
trap restore_device EXIT

capture_locale() {
    local locale="$1" prefix="$2" unit="$3" spell="$4" monster="$5"
    adb shell pm clear "$APP_ID" >/dev/null
    adb shell cmd locale set-app-locales "$APP_ID" --locales "$locale"
    rm -rf "$OUTPUT_DIR/raw/$prefix"
    maestro --device "$device" test --test-output-dir="$OUTPUT_DIR/raw/$prefix" \
        -e APP_ID="$APP_ID" -e DISTANCE_UNIT="$unit" -e SPELL_QUERY="$spell" -e MONSTER_QUERY="$monster" "$FLOW"
    find "$OUTPUT_DIR/raw/$prefix" -path '*takeScreenshot*' -name '*.png' | while read -r screenshot; do
        crop_for_play "$screenshot" "$OUTPUT_DIR/$prefix-$(basename "$screenshot")"
    done
}

# Play caps the aspect ratio at 2:1: keep the top of the app, below the status bar.
crop_for_play() {
    local width
    width="$(sips -g pixelWidth "$1" | awk '/pixelWidth/ { print $2 }')"
    sips -c $((width * 2)) "$width" --cropOffset "$STATUS_BAR_HEIGHT" 0 "$1" --out "$2" >/dev/null
}

mkdir -p "$OUTPUT_DIR"
adb shell svc power stayon true
capture_locale en-US en "Feet" "Fireball" "red dragon"
capture_locale fr-FR fr "Mètres" "Boule de feu" "dragon rouge"
echo "Screenshots written to $OUTPUT_DIR"
