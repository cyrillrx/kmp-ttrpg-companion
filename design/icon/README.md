# App icon

`generate_icon.py` holds the drawing of the launcher icon and writes every asset derived from it:

- the Android adaptive icon layers, `ic_launcher_foreground.xml` and `ic_launcher_monochrome.xml`;
- the iOS icon, 1024 px without alpha channel;
- the Play Store icon, 512 px.

The colors come from `app/androidApp/src/main/res/values/ic_launcher_colors.xml`; the debug build overrides them in its own source set. The bitmaps are cropped to the 72 dp area the Android launcher shows, so the three platforms frame the icon alike.

Regenerate after a change to the drawing or to the release colors (macOS only, the bitmaps are rendered with AppKit):

```bash
python3 design/icon/generate_icon.py
```
