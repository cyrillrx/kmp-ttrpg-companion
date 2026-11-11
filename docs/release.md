# Release

The app reaches family and friends through two channels: Google Play internal testing for Android, and the GitHub Release for the desktop packages. iOS is not distributed: TestFlight and ad hoc builds both need a paid Apple Developer account.

## Publishing a version

1. Merge `feat` and `fix` pull requests into `main` as usual. Their squashed subject becomes a changelog line, so write it for a user.
2. release-please keeps a release pull request open. It bumps the version from those commits (`feat` raises the minor, `fix` the patch, a `!` the major) and updates `CHANGELOG.md`, `version.txt` and `Version.APP_VERSION`.
3. Merge the release pull request. The `Release` workflow then:
    - tags `vX.Y.Z` and drafts the GitHub Release with the changelog section;
    - asks Gemini to rewrite that section for testers, in English and French. Without the key, or if the call fails, Play gets the technical section and the release keeps it alone;
    - packages the DMG (Apple silicon), MSI and DEB, and attaches them to the release;
    - when Play publishing is enabled, builds the signed bundle and rolls it out to the internal testing track, with the tester notes as the "What's new" text;
    - publishes the release once every build has succeeded, with the tester notes above the changelog section. A failed build leaves it a draft: rerunning the failed jobs completes the same version after a transient failure, while a build error needs a `fix` commit, which opens the next release pull request.

Once the repository is configured, nothing else is manual. The version code follows the version (`1.2.3` gives `10203`), so every release raises it.

If a push to `main` leaves the Release workflow stuck, run it by hand with `gh workflow run release.yml`: release-please always works against `main`, whichever branch the run starts from.

## Repository configuration

release-please opens its pull request with the workflow token, so the repository settings must allow GitHub Actions to create pull requests.

Play publishing stays off until the repository variable `PLAY_PUBLISHING_ENABLED` is `true`. Set it only once the secrets below exist and a first release was rolled out by hand to the internal testing track: while the app is a draft, the Play API accepts only draft releases.

| Secret                             | Content                                                                    |
|------------------------------------|----------------------------------------------------------------------------|
| `ANDROID_UPLOAD_KEYSTORE_BASE64`   | The upload keystore, as `base64 -i upload.jks`                             |
| `ANDROID_UPLOAD_KEYSTORE_PASSWORD` | Its password, which is also the key password in a PKCS12 keystore          |
| `ANDROID_UPLOAD_KEY_ALIAS`         | The key alias                                                              |
| `PLAY_SERVICE_ACCOUNT_JSON`        | The JSON key of a Google Cloud service account granted access to the app   |
| `GEMINI_API_KEY`                   | Optional: a free-tier Google AI Studio key, for the tester notes           |

## Installing, for testers

- **Android**: open the opt-in link of the internal testing track with the Google account the maintainer registered, then install from the Play Store. Updates arrive like any other app.
- **macOS**: download the DMG from the latest GitHub Release. The app is not notarized: after the first blocked launch, choose "Open Anyway" in System Settings > Privacy & Security, or run `xattr -dr com.apple.quarantine /Applications/com.cyrillrx.rpg.app`.
- **Windows**: download the MSI. SmartScreen warns about an unknown publisher: choose "More info", then "Run anyway".
- **Linux**: download the DEB and install it with `sudo apt install ./<file>.deb`.

Desktop updates are manual: watch the repository's releases on GitHub to be notified.
