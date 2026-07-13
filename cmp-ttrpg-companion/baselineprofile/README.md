# :baselineprofile

Macrobenchmark module that generates the app's [Baseline Profile](https://developer.android.com/topic/performance/baselineprofiles/overview)
and measures scroll performance of the compendium lists. Targets `:androidApp`.

## Generate the baseline profile

```bash
./gradlew :androidApp:generateBaselineProfile
```

Runs `BaselineProfileGenerator` on the managed device `pixel6Api34` and writes two profiles into
the app: a **baseline profile** (`baseline-prof.txt`, open + scroll each compendium list) and a
**startup profile** (`startup-prof.txt`, the cold-launch path). Profile generation does **not** rely
on frame timing, so the managed (emulator) device is fine here.

## Measure scroll jank (`ScrollBenchmark`)

`FrameTimingMetric` reads frames from `dumpsys gfxinfo framestats`, which emulators (including Gradle
Managed Devices) do **not** report reliably — a run there yields `0 found for frameDurationCpuMs`.
**Use a physical device** for meaningful numbers:

```bash
# with a physical device connected (USB debugging on)
./gradlew :baselineprofile:connectedBenchmarkReleaseAndroidTest
```

Compare `frameDurationCpuMs` (P50/P90/P99) between `scrollNoCompilation` (interpreted, the warmup
case) and `scrollBaselineProfile` (AOT-compiled hot paths).

> The managed-device benchmark task (`pixel6Api34BenchmarkReleaseAndroidTest`) suppresses the
> `EMULATOR` guard so it can run, but its frame timings are not trustworthy — prefer a physical
> device.

## Notes

- Scroll targeting relies on the `compendium_list` testTag exposed as a UiAutomator resource id
  (see `testTagsAsResourceId` in the Android entry point).
- Home-button labels are matched by their default (English) text, matching the managed device locale.
