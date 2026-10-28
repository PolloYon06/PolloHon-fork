<div align="center">

<img src=".github/assets/icon.svg" alt="PolloHon" width="96"/>

# PolloHon

</div>

A personal, unofficial fork of [Mihon](https://github.com/mihonapp/mihon) — a free and open-source
manga, webtoon and comic reader for Android.

PolloHon is not affiliated with Mihon, Tachiyomi, or any content provider. It hosts zero content.

## What's different from upstream

- Rebranded as **PolloHon** (`app.pollohon`) so it can be installed alongside Mihon.
- **No telemetry**: Firebase/Crashlytics are disabled by default and the upstream
  `google-services.json` has been removed.
- **Built-in updater disabled** by default. If you re-enable it, it points at this repository's
  releases only.
- New launcher icon, splash logo and notification glyph (a chicken, because *pollo*).
- Donation and patron prompts removed; nothing in the app asks for money.

Everything else — the reader, library, downloads, tracking, backups and the extension API — is
upstream Mihon code. Extensions built for Mihon/Tachiyomi work unchanged; add an extension store
under **Settings → Browse → Extension stores**.

## First run

Open **Browse → Extensions**. When no store is configured, the empty screen offers
**Add Keiyoushi extension store**; tap it, confirm, and install whatever sources you need.

## Building

Requires JDK 21 and the Android SDK (compile SDK 37.2, build-tools matching).

```bash
./gradlew assembleDebug          # installable debug build
./gradlew assembleRelease        # minified release build (needs signing config)
./gradlew spotlessCheck testDebugUnitTest
```

Output APKs are written to `app/build/outputs/apk/`.

To enable upstream-style features explicitly:

```bash
./gradlew assembleRelease -Pinclude-telemetry -Penable-updater
```

Enabling telemetry requires supplying your own `app/google-services.json`.

## Keeping up with upstream

```bash
git remote add upstream https://github.com/mihonapp/mihon.git
git fetch upstream
git merge upstream/main
```

Changes here are deliberately small and confined to branding, build flags and the about screen, so
merges should stay manageable.

## License and credits

Licensed under the [Apache License 2.0](LICENSE), same as upstream.

Copyright © 2015 Javier Tomás
Copyright © 2024 Mihon Open Source Project
Copyright © 2026 PolloHon contributors

All credit for the application belongs to the [Mihon contributors](https://github.com/mihonapp/mihon/graphs/contributors).
