#!/usr/bin/env python3
import subprocess
import tempfile
import xml.etree.ElementTree as ElementTree
from pathlib import Path

SOURCE_DIR = Path(__file__).resolve().parent
REPO_ROOT = SOURCE_DIR.parents[1]
ANDROID_RES = REPO_ROOT / "app/androidApp/src/main/res"
LAUNCHER_COLORS = ANDROID_RES / "values/ic_launcher_colors.xml"
FOREGROUND_DRAWABLE = ANDROID_RES / "drawable/ic_launcher_foreground.xml"
MONOCHROME_DRAWABLE = ANDROID_RES / "drawable/ic_launcher_monochrome.xml"
IOS_ICON = REPO_ROOT / "app/iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/app-icon-1024.png"
PLAY_STORE_ICON = REPO_ROOT / "app/shared/ui/src/androidMain/ic_launcher-playstore.png"
PNG_RENDERER = SOURCE_DIR / "render_png.swift"

CANVAS = 512
ADAPTIVE_ICON_DP = 108
ADAPTIVE_ICON_VISIBLE_DP = 72
ARTWORK_SCALE = 0.95
ARTWORK_CENTER_X, ARTWORK_CENTER_Y = 260, 255

COVER_COLOR = "@color/ic_launcher_cover"
SPINE_COLOR = "@color/ic_launcher_spine"
MONOCHROME_COLOR = "#FF000000"
GOLD = "#F5BD48"
D20_SHADES = ["#FFE7A8", GOLD, "#E2A52E", "#C98C1E", "#A8731A"]

COVER = "M172,130 H348 A22,22 0 0 1 370,152 V358 A22,22 0 0 1 348,380 H172 A22,22 0 0 1 150,358 V152 A22,22 0 0 1 172,130 Z"
SPINE = "M172,130 H188 V380 H172 A22,22 0 0 1 150,358 V152 A22,22 0 0 1 172,130 Z"
SPINE_GROOVE = "M188,130 H196 V380 H188 Z"
CORNER_ORNAMENTS = [
    "M188,130 H226 A38,38 0 0 1 188,168 Z",
    "M332,130 H348 A22,22 0 0 1 370,152 V168 A38,38 0 0 1 332,130 Z",
    "M188,380 H226 A38,38 0 0 0 188,342 Z",
    "M332,380 H348 A22,22 0 0 0 370,358 V342 A38,38 0 0 0 332,380 Z",
]
SPARKLES = [
    "M150,158 Q154.2,185.8 175.6,190 Q154.2,194.2 150,222 Q145.8,194.2 124.4,190 Q145.8,185.8 150,158 Z",
    "M370,276 Q373.1,296.9 389.2,300 Q373.1,303.1 370,324 Q366.9,303.1 350.8,300 Q366.9,296.9 370,276 Z",
]
D20_CENTER_X, D20_CENTER_Y, D20_RADIUS = 279, 236, 66
D20_VERTICES = {
    "top": (0, -1),
    "upper_right": (0.866, -0.5),
    "lower_right": (0.866, 0.5),
    "bottom": (0, 1),
    "lower_left": (-0.866, 0.5),
    "upper_left": (-0.866, -0.5),
    "inner_top": (0, -0.5),
    "inner_right": (0.433, 0.25),
    "inner_left": (-0.433, 0.25),
}
D20_FACES = [
    ("top upper_left inner_top", 1),
    ("top upper_right inner_top", 1),
    ("upper_left inner_top inner_left", 2),
    ("upper_right inner_top inner_right", 2),
    ("upper_left inner_left lower_left", 3),
    ("upper_right inner_right lower_right", 3),
    ("lower_left inner_left bottom", 4),
    ("lower_right inner_right bottom", 4),
    ("inner_left inner_right bottom", 3),
    ("inner_top inner_right inner_left", 0),
]
D20_OUTLINE = "top upper_right lower_right bottom lower_left upper_left"
D20_FRONT_FACE = "inner_top inner_right inner_left"


def main():
    colors = read_launcher_colors()
    with tempfile.TemporaryDirectory() as work_dir:
        svg = Path(work_dir) / "icon.svg"
        svg.write_text(full_bleed_svg(colors))
        render_png(svg, IOS_ICON, 1024, opaque=True)
        render_png(svg, PLAY_STORE_ICON, 512, opaque=False)
    FOREGROUND_DRAWABLE.write_text(vector_drawable(color_layer()))
    MONOCHROME_DRAWABLE.write_text(vector_drawable(monochrome_layer()))


def read_launcher_colors():
    root = ElementTree.parse(LAUNCHER_COLORS).getroot()
    return {f"@color/{color.get('name')}": color.text for color in root.iter("color")}


def color_layer():
    return (
        [(COVER, COVER_COLOR), (SPINE, SPINE_COLOR)]
        + [(ornament, GOLD) for ornament in CORNER_ORNAMENTS]
        + [(polygon(D20_OUTLINE), GOLD)]
        + [(polygon(face), D20_SHADES[shade]) for face, shade in D20_FACES]
        + [(line, GOLD) for line in title_lines()]
        + [(sparkle, GOLD) for sparkle in SPARKLES]
    )


def monochrome_layer():
    cut_outs = " ".join([COVER, SPINE_GROOVE, polygon(D20_OUTLINE)] + title_lines())
    return [(cut_outs, MONOCHROME_COLOR, "evenOdd"), (polygon(D20_FRONT_FACE), MONOCHROME_COLOR)]


def title_lines():
    return [rounded_bar(237, 320, 84, 10), rounded_bar(237, 340, 84, 10)]


def rounded_bar(x, y, width, height):
    radius = height / 2
    return (
        f"M{x + radius:g},{y} H{x + width - radius:g} A{radius:g},{radius:g} 0 0 1 {x + width - radius:g},{y + height} "
        f"H{x + radius:g} A{radius:g},{radius:g} 0 0 1 {x + radius:g},{y} Z"
    )


def polygon(vertex_names):
    points = [d20_point(name) for name in vertex_names.split()]
    return "M" + " L".join(points) + " Z"


def d20_point(name):
    unit_x, unit_y = D20_VERTICES[name]
    return f"{D20_CENTER_X + unit_x * D20_RADIUS:.1f},{D20_CENTER_Y + unit_y * D20_RADIUS:.1f}"


def vector_drawable(paths):
    dp_per_unit = ADAPTIVE_ICON_DP / CANVAS
    scale = ARTWORK_SCALE * dp_per_unit
    translate_x = (CANVAS / 2 - ARTWORK_SCALE * ARTWORK_CENTER_X) * dp_per_unit
    translate_y = (CANVAS / 2 - ARTWORK_SCALE * ARTWORK_CENTER_Y) * dp_per_unit
    lines = [
        '<vector xmlns:android="http://schemas.android.com/apk/res/android"',
        f'    android:width="{ADAPTIVE_ICON_DP}dp"',
        f'    android:height="{ADAPTIVE_ICON_DP}dp"',
        f'    android:viewportWidth="{ADAPTIVE_ICON_DP}"',
        f'    android:viewportHeight="{ADAPTIVE_ICON_DP}">',
        "    <group",
        f'        android:scaleX="{scale:.6f}"',
        f'        android:scaleY="{scale:.6f}"',
        f'        android:translateX="{translate_x:.4f}"',
        f'        android:translateY="{translate_y:.4f}">',
    ]
    lines += [vector_path(*path) for path in paths]
    lines += ["    </group>", "</vector>", ""]
    return "\n".join(lines)


def vector_path(path_data, fill, fill_type=None):
    attributes = [f'android:fillColor="{fill}"']
    if fill_type:
        attributes.append(f'android:fillType="{fill_type}"')
    attributes.append(f'android:pathData="{path_data}"')
    return "        <path\n            " + "\n            ".join(attributes) + " />"


def full_bleed_svg(colors):
    visible = CANVAS * ADAPTIVE_ICON_VISIBLE_DP / ADAPTIVE_ICON_DP
    margin = (CANVAS - visible) / 2
    paths = "".join(f'<path d="{path}" fill="{resolve_color(fill, colors)}"/>' for path, fill in color_layer())
    return (
        f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="{margin:.4f} {margin:.4f} {visible:.4f} {visible:.4f}">'
        f'<rect width="{CANVAS}" height="{CANVAS}" fill="{colors["@color/ic_launcher_background"]}"/>'
        f'<g transform="translate({CANVAS / 2:g},{CANVAS / 2:g}) scale({ARTWORK_SCALE}) '
        f'translate({-ARTWORK_CENTER_X},{-ARTWORK_CENTER_Y})">{paths}</g></svg>'
    )


def resolve_color(fill, colors):
    if fill.startswith("@color/"):
        return colors[fill]

    return fill


def render_png(svg, output, size, opaque):
    command = ["swift", str(PNG_RENDERER), str(svg), str(output), str(size)]
    if opaque:
        command.append("--opaque")
    subprocess.run(command, check=True)


if __name__ == "__main__":
    main()
