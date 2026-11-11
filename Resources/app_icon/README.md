# App icon

`generate_icon.py` holds the drawing of the launcher icon and writes every asset derived from it:

- the Android adaptive icon layers, `ic_launcher_foreground.xml` and `ic_launcher_monochrome.xml`;
- the iOS icon, 1024 px without alpha channel;
- the Play Store icon, 512 px.

The background, cover and spine colors are read from `app/androidApp/src/main/res/values/ic_launcher_colors.xml`, which the debug build overrides in its own source set. The gold of the ornaments and the shades of the d20 are common to both builds and live in the script. The bitmaps are cropped to the 72 dp area the Android launcher shows, so the three platforms frame the icon alike.

Regenerate after a change to the drawing or to the release colors (macOS 14 or later, the bitmaps are rendered with AppKit):

```bash
python3 Resources/app_icon/generate_icon.py
```
